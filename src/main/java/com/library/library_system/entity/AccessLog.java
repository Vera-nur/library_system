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
    @Column(name = "accesslog_id")
    private Integer accessLogId;

    // Örn: "GIVE_BOOK", "ADD_WORKER", "ADD_USER"; ParameterDef: ACCESSLOG_PROCESS
    @ManyToOne
    @JoinColumn(name = "access_type_id", nullable = false)
    private Parameter process;

    // İşlemi yapan çalışan
    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    // İşlemin ilgili olduğu kullanıcı (kitabı alan kişi)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date", nullable = false)
    private LocalDateTime createdAt;
}