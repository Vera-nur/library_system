package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "person")
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer person_id;

    private String name;
    private String surname;
    private String tel;
    @Column(unique = true, nullable = false)
    private String email;
    private String address;
    @Column(nullable = false)
    private String password;
    private String person_type;
}
