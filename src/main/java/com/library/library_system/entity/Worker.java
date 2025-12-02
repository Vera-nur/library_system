package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "workers")
@Data
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "worker_id")
    private Integer workerId;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", nullable = false)
    private Person person;

    //bu kısmı bir classta yapmak daha mantıklı olabilir
    //@OneToMany(mappedBy = "worker")
    //private List<AccessLog> accessLogs;   // Bu çalışanın yaptığı tüm işlemler
}
