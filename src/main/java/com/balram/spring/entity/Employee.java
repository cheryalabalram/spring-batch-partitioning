package com.balram.spring.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "EMPLOYEE")
@Data
@NoArgsConstructor
//public class Employee implements Persistable<Long>, Serializable {
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String role;

    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }

    // Getters and setters

//    @Override
//    public Long getId() {
//        return id;
//    }
//
//    @Override
//    public boolean isNew() {
//        return id == null;
//    }
}