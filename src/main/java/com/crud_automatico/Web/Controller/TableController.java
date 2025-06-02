package com.crud_automatico.Web.Controller;

import com.crud_automatico.Persistence.Projection.TableProjection;
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
    public ResponseEntity<List<TableProjection>> getAllTables() {
        List<TableProjection> tables = tableService.getAllTables();
        return ResponseEntity.ok(tables);
    }



}
