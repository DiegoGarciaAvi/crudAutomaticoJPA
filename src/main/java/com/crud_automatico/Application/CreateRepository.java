package com.crud_automatico.Application;

import com.crud_automatico.Service.ColumTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class CreateRepository {

    private final ColumTableService columnTableService;

    @Autowired
    public CreateRepository(ColumTableService columTableService) {
        this.columnTableService = columTableService;
    }


    public String createRepository(String entityName,String typeIdEntity){

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

            return repositoryName;

        } catch (IOException e) {
            System.out.println("Error creating the file");
            throw new RuntimeException(e);
        }

    }
}
