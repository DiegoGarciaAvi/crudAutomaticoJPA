package com.crud_automatico.Application;

import com.crud_automatico.Service.ColumTableService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class CreateControllerApi {
    private final ColumTableService columnTableService;

    public CreateControllerApi(ColumTableService columTableService) {
        this.columnTableService = columTableService;
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

            file.write("\n\t@GetMapping(\"/{id}\")\n" +
                    "\tpublic ResponseEntity<?> getById(@PathVariable "+typeIdEntity+" id){\n" +
                    "\n\t\ttry {\n" +
                    "\n\t\t\t"+entityName+" "+objetNameMethods+" = "+objetName+".getById(id);\n" +
                    "\n\t\t\treturn ResponseEntity.ok("+objetNameMethods+");\n" +
                    "\n\t\t} catch (RuntimeException e) {\n" +
                    "\n\t\t\treturn ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());\n" +
                    "\n\t\t}\n" +
                    "\n\t}\n");

            file.write("\n\t@PostMapping\n" +
                    "\tpublic ResponseEntity<?> save(@RequestBody "+entityName+" "+objetNameMethods+"){\n" +
                    "\n\t\ttry {\n" +
                    "\n\t\t\t"+entityName+" "+objetNameMethods+"Save = "+objetName+".save("+objetNameMethods+");\n" +
                    "\n\t\t\treturn ResponseEntity.ok("+objetNameMethods+"Save);\n" +
                    "\n\t\t} catch (RuntimeException e) {\n" +
                    "\n\t\t\treturn ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());\n" +
                    "\n\t\t}\n" +
                    "\n\t}\n");

            file.write("\n\t@PutMapping\n" +
                    "\tpublic ResponseEntity<?> update(@RequestBody "+entityName+" "+objetNameMethods+"){\n" +
                    "\n\t\ttry {\n" +
                    "\n\t\t\t"+entityName+" "+objetNameMethods+"Update = "+objetName+".update("+objetNameMethods+");\n" +
                    "\n\t\t\treturn ResponseEntity.ok("+objetNameMethods+"Update);\n" +
                    "\n\t\t} catch (RuntimeException e) {\n" +
                    "\n\t\t\treturn ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());\n" +
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
