package com.crud_automatico.Web.Controller;

import com.crud_automatico.Persistence.Proyection.ColumTableProyection;
import com.crud_automatico.Persistence.Proyection.ForeingKeyTableProyection;
import com.crud_automatico.Service.ColumTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/columTable")
public class ColumTableController {

    private final ColumTableService columTableService;

    @Autowired
    public ColumTableController(ColumTableService columTableService) {
        this.columTableService = columTableService;
    }

    @GetMapping("/{nameTable}")
    public ResponseEntity<List<ColumTableProyection>> getAllColumTable(@PathVariable String nameTable) {
        List<ColumTableProyection> columTables = columTableService.getAllColumTable(nameTable);
        return ResponseEntity.ok(columTables);
    }

    @GetMapping("/foreingKey/{nameTable}")
    public ResponseEntity<List<ForeingKeyTableProyection>> getAllForeingKeyTable(@PathVariable String nameTable) {
        List<ForeingKeyTableProyection> columTables = columTableService.getAllForeingKeyTable(nameTable);
        return ResponseEntity.ok(columTables);
    }

}
