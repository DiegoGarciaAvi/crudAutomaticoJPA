package com.crud_automatico.Persistence.Repository;

import com.crud_automatico.Persistence.Entity.ColumTableEntity;
import com.crud_automatico.Persistence.Proyection.ColumTableProyection;
import com.crud_automatico.Persistence.Proyection.ForeingKeyTableProyection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumTableRepository extends JpaRepository<ColumTableEntity,Long> {

    @Query(value = "SELECT column_name as ColumTableName, udt_name as UdtName \n" +
            "FROM information_schema.columns \n" +
            "WHERE table_schema = 'public' \n" +
            "AND table_name = :nameTable ; " ,nativeQuery = true)
    List<ColumTableProyection> findAllByTablaId(@Param("nameTable") String nameTable);

    @Query(value = "SELECT \n" +
            "    tc.constraint_name as ConstraintName , \n" +
            "    tc.table_name AS ForeingTable, \n" +
            "    kcu.column_name AS ForeingColumn, \n" +
            "    ccu.table_name AS ReferenceTable, \n" +
            "    ccu.column_name AS ReferenceColumn \n" +
            "FROM " +
            "    information_schema.table_constraints AS tc \n" +
            "JOIN " +
            "    information_schema.key_column_usage AS kcu \n" +
            "    ON tc.constraint_name = kcu.constraint_name \n" +
            "JOIN " +
            "    information_schema.constraint_column_usage AS ccu \n" +
            "    ON ccu.constraint_name = tc.constraint_name \n" +
            "WHERE " +
            "    constraint_type = 'FOREIGN KEY' \n" +
            "    AND tc.constraint_schema = 'public' \n" +
            "    AND tc.table_name=:nameTable;", nativeQuery = true)
    List<ForeingKeyTableProyection> findAllByTablaIdForeingKey(@Param("nameTable") String nameTable);

}
