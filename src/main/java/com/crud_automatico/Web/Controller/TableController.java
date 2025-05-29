package com.crud_automatico.Web.Controller;

import com.crud_automatico.Persistence.Entity.DatabaseInfoLoggerEntity;
import com.crud_automatico.Persistence.Proyection.TableProyection;
import com.crud_automatico.Service.DatabaseInfoLogger;
import com.crud_automatico.Service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tables")
public class TableController {

    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping
    public ResponseEntity<List<TableProyection>> getAllTables() {
        List<TableProyection> tables = tableService.getAllTables();
        return ResponseEntity.ok(tables);
    }



}
