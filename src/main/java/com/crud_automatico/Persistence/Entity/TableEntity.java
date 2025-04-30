package com.crud_automatico.Persistence.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@org.hibernate.annotations.Subselect("SELECT 1")
public class TableEntity {

    @Id
    private Long id;

}
