package com.crud_automatico.Web.Controller;

import com.crud_automatico.Service.BuilderApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/builder-api")
public class BuilderApiController {

    private final BuilderApi builderApi;

    public BuilderApiController(BuilderApi builderApi) {
        this.builderApi = builderApi;
    }

    @GetMapping("/{tableName}")
    public ResponseEntity<String> createApiFiles(@PathVariable String tableName) {
        try {
            builderApi.builderFilesApi(tableName);
            return ResponseEntity.ok("API files created successfully for table: " + tableName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating API files: " + e.getMessage());
        }
    }

}
