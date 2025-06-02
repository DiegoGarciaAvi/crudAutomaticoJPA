package com.crud_automatico.Service;

import com.crud_automatico.Persistence.Entity.DatabaseInfoLoggerEntity;
import com.crud_automatico.Persistence.Projection.TableProjection;
import com.crud_automatico.Persistence.Repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {

    private final TableRepository tablaRepository;
    private final DatabaseInfoLogger databaseInfoLogger;

    @Autowired
    public TableService(TableRepository tablaRepository, DatabaseInfoLogger databaseInfoLogger) {
        this.tablaRepository = tablaRepository;
        this.databaseInfoLogger = databaseInfoLogger;
    }

    public List<TableProjection> getAllTables(){

        DatabaseInfoLoggerEntity dbInfo= databaseInfoLogger.printDatabaseInfo();

        if(dbInfo.getDbProductName().equals("postgresql")){
            return tablaRepository.findAllByTablesNamesPostgres();
        } else if (dbInfo.getDbProductName().equals("mysql")) {
            return tablaRepository.findAllByTablesNamesMySql(dbInfo.getDbName());
        }else{
            throw new RuntimeException("Database not supported: " + dbInfo.getDbProductName());
        }
    }
}
