package com.crud_automatico.Service;

import com.crud_automatico.Persistence.Entity.DatabaseInfoLoggerEntity;
import com.crud_automatico.Persistence.Projection.ColumTableProjection;
import com.crud_automatico.Persistence.Projection.ForeignKeyTableProjection;
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

    public List<ColumTableProjection> getAllColumTable(String nameTable) {

        DatabaseInfoLoggerEntity dbInfo = databaseInfoLogger.printDatabaseInfo();

        if(dbInfo.getDbProductName().equals("postgresql")){
           return columTableRepository.findAllColumnsNamePostgres(nameTable);
        } else if (dbInfo.getDbProductName().equals("mysql")) {
            return columTableRepository.findAllNameColumMySql(dbInfo.getDbName(), nameTable);
        } else {
            throw new RuntimeException("Database not supported: " + dbInfo.getDbProductName());
        }

    }

    public List<ForeignKeyTableProjection> getAllForeignKeyTable(String nameTable) {

        DatabaseInfoLoggerEntity dbInfo = databaseInfoLogger.printDatabaseInfo();
        if(dbInfo.getDbProductName().equals("postgresql")){
            return columTableRepository.findAllColumnsForeingKeyPostgres(nameTable);
        } else if (dbInfo.getDbProductName().equals("mysql")) {
            return columTableRepository.findAllColumnsForeignKeyMySql(dbInfo.getDbName(),nameTable);
        } else {
            throw new RuntimeException("Database not supported: " + dbInfo.getDbProductName());
        }

    }

}
