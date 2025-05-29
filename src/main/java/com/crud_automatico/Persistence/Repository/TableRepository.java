package com.crud_automatico.Persistence.Repository;

import com.crud_automatico.Persistence.Entity.TableEntity;
import com.crud_automatico.Persistence.Proyection.TableProyection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {

    @Query(value = "SELECT TABLE_NAME as tableName \n" +
                    "FROM information_schema.tables\n" +
                    "WHERE table_schema='public' \n" +
                    "AND table_type='BASE TABLE';", nativeQuery = true)
    List<TableProyection> findAllByTablesNamesPostgres();


    @Query(value = "SELECT table_name\n" +
                    "FROM information_schema.tables\n" +
                    "WHERE table_schema =:dbname \n" +
                    "AND table_type = 'BASE TABLE';", nativeQuery = true)
    List<TableProyection> findAllByTablesNamesMySql(@Param("dbname") String dbname);
}
