package com.crud_automatico.Persistence.Repository;

import com.crud_automatico.Persistence.Entity.ColumTableEntity;
import com.crud_automatico.Persistence.Projection.ColumTableProjection;
import com.crud_automatico.Persistence.Projection.ForeignKeyTableProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumTableRepository extends JpaRepository<ColumTableEntity,Long> {

    @Query(value = "SELECT " +
                    "   column_name as ColumTableName, udt_name as UdtName \n" +
                    "FROM information_schema.columns \n" +
                    "WHERE table_schema = 'public' \n" +
                    "   AND table_name = :nameTable ; " ,nativeQuery = true)
    List<ColumTableProjection> findAllColumnsNamePostgres(@Param("nameTable") String nameTable);

    @Query(value = "SELECT \n" +
                    "    tc.constraint_name as ConstraintName , \n" +
                    "    tc.table_name AS ForeignTable, \n" +
                    "    kcu.column_name AS ForeignColumn, \n" +
                    "    ccu.table_name AS ReferenceTable, \n" +
                    "    ccu.column_name AS ReferenceColumn, \n" +
                    "    tc.constraint_type AS ConstraintType \n" +
                    "FROM " +
                    "    information_schema.table_constraints AS tc \n" +
                    "JOIN " +
                    "    information_schema.key_column_usage AS kcu \n" +
                    "    ON tc.constraint_name = kcu.constraint_name \n" +
                    "JOIN " +
                    "    information_schema.constraint_column_usage AS ccu \n" +
                    "    ON ccu.constraint_name = tc.constraint_name \n" +
                    "WHERE " +
                    "    tc.constraint_schema = 'public' \n" +
                    "    AND tc.table_name=:nameTable;", nativeQuery = true)
    List<ForeignKeyTableProjection> findAllColumnsForeingKeyPostgres(@Param("nameTable") String nameTable);


    @Query(value = "SELECT " +
                    "   column_name AS ColumTableName, data_type AS UdtName\n " +
                    "FROM " +
                    "   information_schema.columns\n " +
                    "WHERE table_schema =:dbname\n " +
                    "  AND table_name =:nameTable;\n ", nativeQuery = true)
    List<ColumTableProjection> findAllNameColumMySql(@Param("dbname") String dbname, @Param("nameTable") String nameTable);

    @Query(value = "SELECT\n " +
                "    tc.constraint_name AS ConstraintName,\n " +
                "    tc.table_name AS ForeignTable,\n " +
                "    kcu.column_name AS ForeignColumn,\n " +
                "    kcu.referenced_table_name AS ReferenceTable,\n " +
                "    kcu.referenced_column_name AS ReferenceColumn,\n " +
                "    tc.constraint_type AS ConstraintType\n " +
                "FROM information_schema.table_constraints tc\n " +
                "JOIN information_schema.key_column_usage kcu\n " +
                "    ON tc.constraint_name = kcu.constraint_name\n " +
                "    AND tc.table_name = kcu.table_name\n " +
                "    AND tc.table_schema = kcu.table_schema\n " +
                "WHERE\n " +
                "    tc.constraint_schema =:dbname\n " +
                "    AND tc.table_name =:nameTable\n " +
                "    AND tc.constraint_type IN ('PRIMARY KEY', 'FOREIGN KEY')\n " +
                "ORDER BY\n " +
                "    tc.constraint_type, tc.constraint_name, kcu.ordinal_position;\n ", nativeQuery = true)
    List<ForeignKeyTableProjection> findAllColumnsForeignKeyMySql(@Param("dbname") String dbname, @Param("nameTable") String nameTable);
}
