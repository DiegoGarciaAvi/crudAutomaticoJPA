package com.crud_automatico.Persistence.Projection;

public interface ForeignKeyTableProjection {

    String getConstraintName();
    String getForeignTable();
    String getForeignColumn();
    String getReferenceTable();
    String getReferenceColumn();
    String getConstraintType();
}
