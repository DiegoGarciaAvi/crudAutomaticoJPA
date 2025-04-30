package com.crud_automatico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class CrudAutomaticoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudAutomaticoApplication.class, args);
	}

}
