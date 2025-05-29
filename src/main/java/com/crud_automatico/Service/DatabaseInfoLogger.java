package com.crud_automatico.Service;

import com.crud_automatico.Persistence.Entity.DatabaseInfoLoggerEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@Component
public class DatabaseInfoLogger {

    @Autowired
    private DataSource dataSource;

    private final DatabaseInfoLoggerEntity databaseInfoLoggerEntity;

    @Autowired
    public DatabaseInfoLogger(DatabaseInfoLoggerEntity databaseInfoLoggerEntity) {
        this.databaseInfoLoggerEntity = databaseInfoLoggerEntity;
    }

    @PostConstruct
    public DatabaseInfoLoggerEntity printDatabaseInfo() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            String dbProductName = metaData.getDatabaseProductName().toLowerCase(); // Ej: MySQL, PostgreSQL
            String dbname = metaData.getURL().split("/")[3];

            databaseInfoLoggerEntity.setDbProductName(dbProductName);
            databaseInfoLoggerEntity.setDbName(dbname);

            return databaseInfoLoggerEntity;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la información de la base de datos", e);
        }
    }
}