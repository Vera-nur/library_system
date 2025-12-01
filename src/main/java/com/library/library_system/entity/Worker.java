package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workers")
@Data
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer worker_id;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person person;
}
