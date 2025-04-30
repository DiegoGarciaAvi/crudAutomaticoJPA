package com.crud_automatico.Service;

import com.crud_automatico.Persistence.Proyection.TableProyection;
import com.crud_automatico.Persistence.Repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {

    private final TableRepository tablaRepository;

    @Autowired
    public TableService(TableRepository tablaRepository) {
        this.tablaRepository = tablaRepository;
    }

    public List<TableProyection> getAllTables(){
        return tablaRepository.findAllByName();

    }

}
