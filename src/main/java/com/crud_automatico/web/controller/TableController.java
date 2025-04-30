package com.crud_automatico.web.controller;

import com.crud_automatico.Application.CreateFiles;
import com.crud_automatico.Persistence.Proyection.TableProyection;
import com.crud_automatico.Service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tables")
public class TableController {

    private final TableService tableService;
    private final CreateFiles createFiles;

    @Autowired
    public TableController(TableService tableService, CreateFiles createFiles) {
        this.tableService = tableService;
        this.createFiles = createFiles;
    }

    @GetMapping
    public ResponseEntity<List<TableProyection>> getAllTables() {
        List<TableProyection> tables = tableService.getAllTables();
        return ResponseEntity.ok(tables);
    }

    @PostMapping("/files/{nameTable}")
    public ResponseEntity<String> creteFiles(@PathVariable String nameTable) {
        try {
            createFiles.createEntity(nameTable);
            return ResponseEntity.ok("File created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
