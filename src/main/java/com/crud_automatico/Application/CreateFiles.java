package com.crud_automatico.Application;

import com.crud_automatico.Persistence.Proyection.ColumTableProyection;
import com.crud_automatico.Service.ColumTableService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class CreateFiles {

    private final ColumTableService columTableService;

    CreateFiles(ColumTableService columTableService) {
        this.columTableService = columTableService;
    }

    public void createEntity(String tableName) {

        String entityName = tableName.toUpperCase().charAt(0)+tableName.substring(1).toLowerCase() + "Entity";
        String typeIdEntity = "";
        List<ColumTableProyection> columTableEntities= columTableService.getAllColumTable(tableName);
        String path="src/main/java/com/crud_automatico/Persistence/Entity/" + entityName + ".java";

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
                        "@Entity\n" +
                        "@Table(name = \""+tableName+"\" )\n" +
                        "@Setter\n" +
                        "@Getter\n" +
                        "@NoArgsConstructor\n" +
                        "public class " + entityName+" {\n"//CAMBIAR NOMBRE DE LA CLASE
                    );

            for (ColumTableProyection columTableEntity : columTableEntities) {

                String nameColum = columTableEntity.getColumTableName();
                String typeColum = columTableEntity.getUdtName();

                if(nameColum.equals("id") && (typeColum.equals("int2") || typeColum.equals("int4")) || typeColum.equals("int8")){
                    file.write("\n\t@Id");
                    file.write("\n\t@GeneratedValue(strategy = GenerationType.AUTO)");
                    typeIdEntity = "Integer";
                } else if (nameColum.equals("id") && (typeColum.equals("varchar") )) {
                    file.write("\n\t@Id");
                    typeIdEntity = "String ";
                }

                if(nameColum.contains("_")){

                    file.write("\n\t@Column(name = \""+nameColum+"\")");
                    String[] parts = nameColum.split("_");
                    StringBuilder unionParts = new StringBuilder();
                    for(int i = 1; i < parts.length; i++){
                        unionParts.append(parts[i].toUpperCase().charAt(0)).append(parts[i].substring(1).toLowerCase());
                    }
                    nameColum = parts[0]+ unionParts;
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

                if(typeColum.equals("timestamp") || typeColum.equals("timestamptz")){
                    typeColum="LocalDateTime";
                }

                file.write("\n\tprivate " + typeColum + " " + nameColum + ";\n");

            }

            file.write("\n }");
            file.close();

            createRepository(entityName,typeIdEntity);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createRepository(String entityName,String typeIdEntity){

        String repositoryName = entityName.replace("Entity","Repository");
        String path="src/main/java/com/crud_automatico/Persistence/Repository/" + repositoryName + ".java";

        try {

            if(new File(path).exists()){
                throw new RuntimeException("The file already exists");
            }

            FileWriter file = new FileWriter(path);

            file.write("package com.crud_automatico.Persistence.Repository;\n" +
                            "import com.crud_automatico.Persistence.Entity."+entityName+";\n" +
                            "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                            "import org.springframework.stereotype.Repository;\n" +
                            "@Repository\n" +
                            "public interface "+repositoryName+" extends JpaRepository<"+entityName+","+typeIdEntity+"> {\n" +
                            "\n" +
                            "}");

            file.close();

            createService(repositoryName,entityName,typeIdEntity);

        } catch (IOException e) {
            System.out.println("Error creating the file");
            throw new RuntimeException(e);
        }

    }

    public void createService(String repositoryName,String entityName,String typeIdEntity){

        String serviceName = entityName.replace("Entity","Service");
        String path="src/main/java/com/crud_automatico/Service/" + serviceName + ".java";
        String objetName=repositoryName.toLowerCase();
        String objetNameMethods=objetName.replace("repository","");

        if(typeIdEntity.equals("Integer")){
            typeIdEntity="int";
        }

        try {

            if(new File(path).exists()){
                throw new RuntimeException("The file already exists");
            }

            FileWriter file = new FileWriter(path);

            file.write("package com.crud_automatico.Service;\n" +
                            "import com.crud_automatico.Persistence.Entity."+entityName+";\n" +
                            "import com.crud_automatico.Persistence.Repository."+repositoryName+";\n" +
                            "import org.springframework.beans.factory.annotation.Autowired;\n" +
                            "import org.springframework.stereotype.Service;" +
                            "import java.util.List;\n\n" +
                            "@Service\n" +
                            "public class "+serviceName+" {\n" +
                            "\n\tprivate final "+repositoryName+" "+objetName+";\n" +
                            "\n\t@Autowired" +
                            "\n\tpublic "+serviceName+"("+repositoryName+" "+objetName+") {" +
                            "\n\tthis."+objetName+" = "+objetName+";" +
                            "\n\t}\n");

            file.write("\n\tpublic List<"+entityName+"> getAll(){\n" +
                            "\t\treturn "+ objetName+".findAll();\n" +
                            "\t}\n");

            file.write("\n\tpublic "+entityName+ " save("+entityName+" "+objetNameMethods+"){\n" +
                            "\t\ttry{" +
                            "\n\t\t\treturn "+ objetName+".save("+objetNameMethods+");\n" +
                            "\t\t} catch (RuntimeException e) {\n"+
                            "\t\t\tthrow new RuntimeException(e);"+
                            "\n\t\t}" +
                            "\n\t}\n");

            file.write("\n\tpublic "+entityName+ " update("+entityName+" "+objetNameMethods+"){\n" +
                            "\t\treturn "+ objetName+".save("+objetNameMethods+");\n" +
                            "\t}\n");

            file.write("\n\tpublic boolean delete("+typeIdEntity+" id){\n" +
                            "\t\ttry{" +
                            "\n\t\t\tif(!"+objetName+".existsById(id)){" +
                            "\n\t\t\t\treturn false;" +
                            "\n\t\t\t}" +
                            "\n\t\t\t"+objetName+".deleteById(id);"+
                            "\n\t\t\treturn true;"+
                            "\n\t\t} catch(Exception e){" +
                            "\n\t\t\tthrow new RuntimeException(e);" +
                            "\n\t\t}"+
                            "\n\t}");

            file.write("\n}");
            file.close();

            createController();

        } catch (IOException e) {
            System.out.println("Error creating the file");
            throw new RuntimeException(e);
        }

    }

    public void createController(){

    }

}
