package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "access_log")
@Data
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_id")
    private Integer accessId;

    @ManyToOne
    @JoinColumn(name = "access_type_id", nullable = false)
    private Parameter accessType;

    // İşlemi yapan çalışan
    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = true)
    private Worker worker;

    // İşlemin ilgili olduğu kullanıcı (kitabı alan kişi)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "date", nullable = false)
    private LocalDateTime createdAt;
}