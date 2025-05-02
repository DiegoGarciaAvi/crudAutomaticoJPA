package com.crud_automatico.Persistence.Proyection;

public interface ForeingKeyTableProyection {

    String getConstraintName();
    String getForeingTable();
    String getForeingColumn();
    String getReferenceTable();
    String getReferenceColumn();
}
