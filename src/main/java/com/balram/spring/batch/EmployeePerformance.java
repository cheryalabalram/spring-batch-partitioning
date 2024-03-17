package com.balram.spring.batch;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

@Entity
@Table(name = "EMPLOYEE_PERFORMANCE")
@Data
@NoArgsConstructor
public class EmployeePerformance implements Persistable<Long>, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String role;

	public EmployeePerformance(String name, String role) {
		this.name = name;
		this.role = role;
	}

	// Getters and setters

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public boolean isNew() {
		return id == null;
	}
}