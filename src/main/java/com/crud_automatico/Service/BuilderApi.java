package com.crud_automatico.Service;

import com.crud_automatico.Application.CreateControllerApi;
import com.crud_automatico.Application.CreateEntity;
import com.crud_automatico.Application.CreateRepository;
import com.crud_automatico.Application.CreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuilderApi {

    private String tableName;
    private final CreateEntity createEntity;
    private final CreateRepository createRepository;
    private final CreateService createService;
    private final CreateControllerApi createControllerApi;

    @Autowired
    public BuilderApi(CreateEntity createEntity, CreateRepository createRepository, CreateService createService, CreateControllerApi createControllerApi) {
        this.createEntity = createEntity;
        this.createRepository = createRepository;
        this.createService = createService;
        this.createControllerApi = createControllerApi;
    }

    public void BuilderFilesApi(String tableName) {

        String entityName = tableName.toUpperCase().charAt(0)+tableName.substring(1).toLowerCase() + "Entity";
        if(tableName.contains("_")){
            String[] partsTableName=tableName.split("_");
            StringBuilder unionPartsTableName=new StringBuilder();
            for (String str : partsTableName) {
                unionPartsTableName.append(str.toUpperCase().charAt(0)).append(str.substring(1).toLowerCase());
            }
            entityName=unionPartsTableName+"Entity";

        }

        String typeIdEntity = createEntity.createEntity(tableName);
        String repositoryName =  createRepository.createRepository(entityName,typeIdEntity);
        String serviceName = createService.createService(repositoryName, entityName, typeIdEntity);
        createControllerApi.createController(serviceName, entityName, typeIdEntity);

    }

}
