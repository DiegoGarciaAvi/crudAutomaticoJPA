package com.crud_automatico.Web.Controller;

import com.crud_automatico.Persistence.Projection.ColumTableProjection;
import com.crud_automatico.Persistence.Projection.ForeignKeyTableProjection;
import com.crud_automatico.Service.ColumTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/columns")
public class ColumTableController {

    private final ColumTableService columTableService;

    @Autowired
    public ColumTableController(ColumTableService columTableService) {
        this.columTableService = columTableService;
    }

    @GetMapping("/{nameTable}")
    public ResponseEntity<List<ColumTableProjection>> getAllColumTable(@PathVariable String nameTable) {
        List<ColumTableProjection> columTables = columTableService.getAllColumTable(nameTable);
        return ResponseEntity.ok(columTables);
    }

    @GetMapping("/foreignKey/{nameTable}")
    public ResponseEntity<List<ForeignKeyTableProjection>> getAllForeignKeyTable(@PathVariable String nameTable) {
        List<ForeignKeyTableProjection> columTables = columTableService.getAllForeingKeyTable(nameTable);
        return ResponseEntity.ok(columTables);
    }

}
