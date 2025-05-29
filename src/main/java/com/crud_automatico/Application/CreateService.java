package com.crud_automatico.Application;

import com.crud_automatico.Service.ColumTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class CreateService {

    private final ColumTableService columTableService;

    @Autowired
    public CreateService(ColumTableService columTableService) {
        this.columTableService = columTableService;
    }

    public String createService(String repositoryName,String entityName,String typeIdEntity){

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

            file.write("\n\tpublic "+entityName+" getById("+typeIdEntity+" id){" +
                    "\n\t\ttry{" +
                    "\n\t\t\treturn "+ objetName+".findById(id).orElseThrow(() -> new RuntimeException(\"Id not found\"));" +
                    "\n\t\t} catch (RuntimeException e) {" +
                    "\n\t\t\tthrow new RuntimeException(\"Error getting by id\"+e.getMessage() );"+
                    "\n\t\t}" +
                    "\n\t}\n");

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

            return serviceName;

        } catch (IOException e) {
            System.out.println("Error creating the file");
            throw new RuntimeException(e);
        }

    }
}
