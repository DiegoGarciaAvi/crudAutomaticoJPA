package com.crud_automatico.Service;

import com.crud_automatico.Persistence.Entity.DatabaseInfoLoggerEntity;
import com.crud_automatico.Persistence.Proyection.ColumTableProyection;
import com.crud_automatico.Persistence.Proyection.ForeingKeyTableProyection;
import com.crud_automatico.Persistence.Repository.ColumTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumTableService {

    private final ColumTableRepository columTableRepository;
    private final DatabaseInfoLogger databaseInfoLogger;

    @Autowired
    public ColumTableService(ColumTableRepository columTableRepository, DatabaseInfoLogger databaseInfoLogger) {
        this.columTableRepository = columTableRepository;
        this.databaseInfoLogger = databaseInfoLogger;
    }

    public List<ColumTableProyection> getAllColumTable(String nameTable) {

        DatabaseInfoLoggerEntity dbInfo = databaseInfoLogger.printDatabaseInfo();

        if(dbInfo.getDbProductName().equals("postgres")){
           return columTableRepository.findAllColumsNamePostgres(nameTable);
        } else if (dbInfo.getDbProductName().equals("mysql")) {
            return columTableRepository.findAllNameColumMySql(dbInfo.getDbName(), nameTable);
        } else {
            throw new RuntimeException("Database not supported: " + dbInfo.getDbProductName());
        }

    }

    public List<ForeingKeyTableProyection> getAllForeingKeyTable(String nameTable) {

        DatabaseInfoLoggerEntity dbInfo = databaseInfoLogger.printDatabaseInfo();
        if(dbInfo.getDbProductName().equals("postgres")){
            return columTableRepository.findAllColumsForeingKeyPostgres(nameTable);
        } else if (dbInfo.getDbProductName().equals("mysql")) {
            return columTableRepository.findAllColumsForeingKeyMySql(dbInfo.getDbName(),nameTable);
        } else {
            throw new RuntimeException("Database not supported: " + dbInfo.getDbProductName());
        }

    }

}
