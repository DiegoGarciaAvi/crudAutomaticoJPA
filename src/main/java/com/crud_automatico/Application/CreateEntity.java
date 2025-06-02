package com.crud_automatico.Application;

import com.crud_automatico.Persistence.Projection.ColumTableProjection;
import com.crud_automatico.Persistence.Projection.ForeignKeyTableProjection;
import com.crud_automatico.Service.ColumTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreateEntity {

    private final ColumTableService columTableService;

    @Autowired
    public CreateEntity(ColumTableService columTableService) {
        this.columTableService = columTableService;
    }

    public String createEntity(String tableName) {

        String entityName = tableName.toUpperCase().charAt(0)+tableName.substring(1).toLowerCase() + "Entity";
        if(tableName.contains("_")){
            String[] partsTableName=tableName.split("_");
            StringBuilder unionPartsTableName=new StringBuilder();
            for (String str : partsTableName) {
                unionPartsTableName.append(str.toUpperCase().charAt(0)).append(str.substring(1).toLowerCase());
            }
            entityName=unionPartsTableName+"Entity";

        }

        String typeIdEntity = "";
        List<ColumTableProjection> columTableEntities= columTableService.getAllColumTable(tableName);
        if (columTableEntities.isEmpty()) {
            throw new RuntimeException("The table does not have columns or does not exist");
        }

        List<ForeignKeyTableProjection> allKeys= columTableService.getAllForeingKeyTable(tableName);
        ArrayList<ForeignKeyTableProjection> primaryKeyTableEntities= new ArrayList<>();
        ArrayList<ForeignKeyTableProjection> foreingKeyTableEntities= new ArrayList<>();

        for (ForeignKeyTableProjection foreingKey : allKeys) {
            if(foreingKey.getConstraintType().equals("FOREIGN KEY")){
                foreingKeyTableEntities.add(foreingKey);
            } else if (foreingKey.getConstraintType().equals("PRIMARY KEY")) {
                primaryKeyTableEntities.add(foreingKey);
            }
        }

        String path="src/main/java/com/crud_automatico/Persistence/Entity/" + entityName + ".java";
        boolean isForeingKey = !foreingKeyTableEntities.isEmpty();

        try {

            if(new File(path).exists()){
                throw new RuntimeException("The file already exists");
            }

            FileWriter file = new FileWriter(path);

            file.write("package com.crud_automatico.Persistence.Entity;\n" +
                    "import jakarta.persistence.*;\n" +
                    "import lombok.Getter;\n" +
                    "import lombok.NoArgsConstructor;\n" +
                    "import lombok.Setter;\n"+
                    "import java.time.LocalDateTime;\n" +
                    "@Entity\n" +
                    "@Table(name = \""+tableName+"\" )\n" +
                    "@Setter\n" +
                    "@Getter\n" +
                    "@NoArgsConstructor\n" +
                    "public class " + entityName+" {\n"
            );

            for (ColumTableProjection columTableEntity : columTableEntities) {

                String nameColum = columTableEntity.getColumTableName();
                String typeColum = columTableEntity.getUdtName();

                for (ForeignKeyTableProjection primaryKey: primaryKeyTableEntities){
                    if(primaryKey.getForeignColumn().equals(nameColum)){
                        if(typeColum.equals("int") || typeColum.equals("int2") || typeColum.equals("int4") || typeColum.equals("int8")){
                            file.write("\n\t@Id");
                            file.write("\n\t@GeneratedValue(strategy = GenerationType.AUTO)");
                            typeIdEntity = "Integer";
                        } else if (typeColum.equals("varchar")) {
                            file.write("\n\t@Id");
                            typeIdEntity = "String ";
                        }
                    }
                }

                if(nameColum.contains("_")){

                    file.write("\n\t@Column(name = \""+nameColum+"\")");
                    String[] parts = nameColum.split("_");
                    StringBuilder unionParts = new StringBuilder();
                    for(int i = 1; i < parts.length; i++){
                        unionParts.append(parts[i].toUpperCase().charAt(0)).append(parts[i].substring(1).toLowerCase());
                    }
                    nameColum = parts[0]+ unionParts;
                } else{
                    file.write("\n\t@Column(name = \""+nameColum+"\")");
                }

                if (typeColum.equals("int2") || typeColum.equals("int4") || typeColum.equals("int8")) {
                    typeColum = "Integer";
                }

                if (typeColum.equals("varchar") || typeColum.equals("text")) {
                    typeColum = "String";
                }

                if (typeColum.equals("float4") || typeColum.equals("float8")) {
                    typeColum = "Float";
                }

                if(typeColum.equals("date")){
                    typeColum="Date";
                }

                if(typeColum.equals("timestamp") || typeColum.equals("timestamptz")|| typeColum.equals("time")){
                    typeColum="LocalDateTime";
                }

                if(typeColum.equals("bool")){
                    typeColum="Boolean";
                }

                file.write("\n\tprivate " + typeColum + " " + nameColum + ";\n");

                if(isForeingKey){
                    for (ForeignKeyTableProjection foreingKey : foreingKeyTableEntities) {

                        String foreingKeyName = columTableEntity.getColumTableName();

                        if(foreingKey.getForeignColumn().equals(foreingKeyName)){

                            String nameForeingKey=foreingKeyName;
                            String nameEntityReference="";

                            if(foreingKeyName.contains("_")){
                                String[] partsFk= foreingKeyName.split("_");
                                StringBuilder unionPartsFk = new StringBuilder();
                                for (int i= 1; i< partsFk.length ;i++) {
                                    unionPartsFk.append(partsFk[i].toUpperCase().charAt(0)).append(partsFk[i].substring(1).toLowerCase());
                                }
                                nameForeingKey=partsFk[0]+unionPartsFk+"Fk";
                            }

                            String[] partsEntity= foreingKey.getReferenceTable().split("_");
                            StringBuilder unionPartsEntity = new StringBuilder();
                            for (String str : partsEntity) {
                                unionPartsEntity.append(str.toUpperCase().charAt(0)).append(str.substring(1).toLowerCase());
                            }
                            nameEntityReference = unionPartsEntity + "Entity";

                            file.write("\n\t@ManyToOne"+
                                    "\n\t@JoinColumn(name = \""+foreingKeyName+"\",columnDefinition = \""+foreingKeyName+"\",insertable = false,updatable = false)"+
                                    "\n\tprivate "+nameEntityReference+" "+nameForeingKey+";\n"
                            );
                        }
                    }
                }

            }

            if(typeIdEntity.isEmpty()){
                throw new RuntimeException("The table does not have a primary key");
            }

            file.write("\n }");
            file.close();
            return typeIdEntity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
