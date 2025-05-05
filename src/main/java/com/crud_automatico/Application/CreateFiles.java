package com.crud_automatico.Application;

import com.crud_automatico.Persistence.Proyection.ColumTableProyection;
import com.crud_automatico.Persistence.Proyection.ForeingKeyTableProyection;
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
        List<ForeingKeyTableProyection> foreingKeyTableEntities= columTableService.getAllForeingKeyTable(tableName);
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
                        "import java.time.LocalDateTime;\n" +
                        "@Entity\n" +
                        "@Table(name = \""+tableName+"\" )\n" +
                        "@Setter\n" +
                        "@Getter\n" +
                        "@NoArgsConstructor\n" +
                        "public class " + entityName+" {\n"
                    );

            for (ColumTableProyection columTableEntity : columTableEntities) {

                String nameColum = columTableEntity.getColumTableName();
                String typeColum = columTableEntity.getUdtName();
                boolean isForeingKey = !foreingKeyTableEntities.isEmpty();

                if(nameColum.equals("id") && (typeColum.equals("int2") || typeColum.equals("int4") || typeColum.equals("int8"))){
                    file.write("\n\t@Id");
                    file.write("\n\t@GeneratedValue(strategy = GenerationType.AUTO)");
                    typeIdEntity = "Integer";
                } else if (nameColum.equals("id") && (typeColum.equals("varchar") )) {
                    file.write("\n\t@Id");
                    typeIdEntity = "String ";
                }

                if(nameColum.contains("_") && !isForeingKey){

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

                if(typeColum.equals("timestamp") || typeColum.equals("timestamptz")){
                    typeColum="LocalDateTime";
                }

                file.write("\n\tprivate " + typeColum + " " + nameColum + ";\n");

                if(isForeingKey){
                    for (ForeingKeyTableProyection foreingKey : foreingKeyTableEntities) {
                        if(foreingKey.getForeingColumn().equals(nameColum)){

                            String nameEntityReference=foreingKey.getReferenceTable().toUpperCase().charAt(0)+foreingKey.getReferenceTable().substring(1).toLowerCase() + "Entity";
                            file.write("\n\t@ManyToOne"+
                                            "\n\t@JoinColumn(name = \""+nameColum+"\",columnDefinition = \""+nameColum+"\",insertable = false,updatable = false)"+
                                            "\n\tprivate "+nameEntityReference+" "+foreingKey.getReferenceTable()+";\n"
                            );
                        }
                    }
                }

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
                            "\t\t\tthrow new RuntimeException(\"Error saving \"+e.getMessage() );"+
                            "\n\t\t}" +
                            "\n\t}\n");

            file.write("\n\tpublic "+entityName+ " update("+entityName+" "+objetNameMethods+"){" +
                            "\t\ttry {" +
                            "\n\t\t\tif (!"+objetName+".existsById("+objetNameMethods+".getId())) {" +
                            "\n\t\t\t\tthrow new RuntimeException(\"Id does not exist\");" +
                            "\n\t\t\t}" +
                            "\n\t\t\treturn "+ objetName+".save("+objetNameMethods+");" +
                            "\n\t\t} catch (RuntimeException e) {"+
                            "\n\t\t\tthrow new RuntimeException(\"Error updating \"+e.getMessage() );"+
                            "\n\t\t}" +
                            "\n\t}\n");

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

            createController(serviceName,entityName,typeIdEntity);

        } catch (IOException e) {
            System.out.println("Error creating the file");
            throw new RuntimeException(e);
        }

    }

    public void createController(String serviceName,String entityName, String typeIdEntity){

        String controllerName = serviceName.replace("Service","Controller");
        String path="src/main/java/com/crud_automatico/Web/Controller/" + controllerName + ".java";
        String mappingName=controllerName.toLowerCase().replace("controller","");
        String objetName=mappingName+"Service";
        String objetNameMethods=objetName.replace("Service","");


        try {

            if(new File(path).exists()){
                throw new RuntimeException("The file already exists");
            }

            FileWriter file = new FileWriter(path);

            file.write("package com.crud_automatico.Web.Controller;\n" +
                            "import com.crud_automatico.Persistence.Entity."+entityName+";\n" +
                            "import com.crud_automatico.Service."+serviceName+";\n" +
                            "import org.springframework.beans.factory.annotation.Autowired;\n" +
                            "import org.springframework.http.HttpStatus;\n" +
                            "import org.springframework.http.ResponseEntity;\n" +
                            "import org.springframework.web.bind.annotation.*;\n" +
                            "import java.util.List;\n\n" +
                            "@RestController\n" +
                            "@RequestMapping(\"/"+mappingName+"\")\n" +
                            "public class "+controllerName+" {\n" +
                            "\n\tprivate final "+serviceName+" "+objetName+";\n" +
                            "\n\t@Autowired" +
                            "\n\tpublic "+controllerName+"("+serviceName+" "+objetName+") {" +
                            "\n\t\tthis."+objetName+" = "+objetName+";" +
                            "\n\t}\n");

            file.write("\n\t@GetMapping\n" +
                            "\tpublic ResponseEntity<List<"+entityName+">> getAll(){\n" +
                            "\n\t\tList<"+entityName+"> "+objetNameMethods+" = "+objetName+".getAll();\n" +
                            "\n\t\treturn ResponseEntity.ok("+objetNameMethods+");\n" +
                            "\n\t}\n");

            file.write("\n\t@PostMapping\n" +
                            "\tpublic ResponseEntity<?> save(@RequestBody "+entityName+" "+objetNameMethods+"){\n" +
                            "\n\t\ttry {\n" +
                            "\n\t\t\t"+entityName+" "+objetNameMethods+"Save = "+objetName+".save("+objetNameMethods+");\n" +
                            "\n\t\t\treturn ResponseEntity.ok("+objetNameMethods+"Save);\n" +
                            "\n\t\t} catch (RuntimeException e) {\n" +
                            "\n\t\t\treturn ResponseEntity.status(HttpStatus.BAD_REQUEST).body(\"Error: \" + e.getMessage());\n" +
                            "\n\t\t}\n" +
                            "\n\t}\n");

            file.write("\n\t@PutMapping\n" +
                            "\tpublic ResponseEntity<?> update(@RequestBody "+entityName+" "+objetNameMethods+"){\n" +
                            "\n\t\ttry {\n" +
                            "\n\t\t\t"+entityName+" "+objetNameMethods+"Update = "+objetName+".update("+objetNameMethods+");\n" +
                            "\n\t\t\treturn ResponseEntity.ok("+objetNameMethods+"Update);\n" +
                            "\n\t\t} catch (RuntimeException e) {\n" +
                            "\n\t\t\treturn ResponseEntity.status(HttpStatus.BAD_REQUEST).body(\"Error: \" + e.getMessage());\n" +
                            "\n\t\t}\n" +
                            "\n\t}\n");

            file.write("\n\t@DeleteMapping(\"/{id}\")\n" +
                            "\tpublic ResponseEntity<String> delete(@PathVariable "+typeIdEntity+" id){\n" +
                            "\n\t\tif("+objetName+".delete(id)){\n" +
                            "\n\t\t\treturn ResponseEntity.ok(\"Deleted successfully\");\n" +
                            "\n\t\t} else {\n" +
                            "\n\t\t\treturn ResponseEntity.status(404).body(\"Not found\");\n" +
                            "\n\t\t}\n" +
                            "\n\t}\n");

            file.write("\n}");
            file.close();

    } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
