package com.crud_automatico.Persistence.Entity;

import jakarta.persistence.Id;

public class ForeingKeyTableEntity {

    @Id
    private String id;
    private String constraintName;
    private String foreingTable;
    private String foreingColumn;
    private String referenceTable;
    private String referenceColumn;
    private String constraintType;

}
