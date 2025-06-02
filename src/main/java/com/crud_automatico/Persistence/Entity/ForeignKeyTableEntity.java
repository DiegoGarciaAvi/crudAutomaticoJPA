package com.crud_automatico.Persistence.Entity;

import jakarta.persistence.Id;

public class ForeignKeyTableEntity {

    @Id
    private String id;
    private String constraintName;
    private String foreignTable;
    private String foreignColumn;
    private String referenceTable;
    private String referenceColumn;
    private String constraintType;

}
