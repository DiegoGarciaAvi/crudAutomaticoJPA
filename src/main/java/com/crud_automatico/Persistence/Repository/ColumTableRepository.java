package com.crud_automatico.Persistence.Repository;

import com.crud_automatico.Persistence.Entity.ColumTableEntity;
import com.crud_automatico.Persistence.Proyection.ColumTableProyection;
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

}
