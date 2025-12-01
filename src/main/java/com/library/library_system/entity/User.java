package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person person;
}

