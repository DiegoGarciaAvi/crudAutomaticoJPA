package com.crud_automatico.Persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tester" )
@Setter
@Getter
@NoArgsConstructor
public class TesterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "full_name_one")
	private String fullNameOne;

	private Integer age;

	private Float cash;

	@Column(name = "born_date")
	private LocalDateTime bornDate;

	@Column(name = "create_at")
	private LocalDateTime createAt;

 }