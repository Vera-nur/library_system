package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "person")
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    private String tel;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String password;

    @Column(name = "person_type")
    private String personType; // 'user' veya 'worker'
}
