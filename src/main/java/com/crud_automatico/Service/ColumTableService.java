package com.crud_automatico.Service;

import com.crud_automatico.Persistence.Proyection.ColumTableProyection;
import com.crud_automatico.Persistence.Repository.ColumTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumTableService {

    private final ColumTableRepository columTableRepository;

    @Autowired
    public ColumTableService(ColumTableRepository columTableRepository) {
        this.columTableRepository = columTableRepository;
    }

    public List<ColumTableProyection> getAllColumTable(String nameTable) {
        return columTableRepository.findAllByTablaId(nameTable);
    }

}
