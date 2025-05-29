package com.crud_automatico.Persistence.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@org.hibernate.annotations.Subselect("SELECT 1")
@Setter
@Getter
public class DatabaseInfoLoggerEntity {

    @Id
    public String dbProductName;

    public String dbName;



}
