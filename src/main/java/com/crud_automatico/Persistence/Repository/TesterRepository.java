package com.crud_automatico.Persistence.Repository;
import com.crud_automatico.Persistence.Entity.TesterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TesterRepository extends JpaRepository<TesterEntity,Integer> {

}