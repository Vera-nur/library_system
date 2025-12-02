package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "digital_access_log")
@Data
public class DigitalBookAccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "digital_access_id")
    private Integer digitalAccessId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;   // Dijital kitabı hangi kullanıcı açtı

    @ManyToOne
    @JoinColumn(name = "digital_book_id", nullable = false)
    private DigitalBook digitalBook;  // Hangi dijital kitap

    // Örn: "OPEN", "DOWNLOAD"; ParameterDef: DIGITAL_ACCESS_TYPE
    @ManyToOne
    @JoinColumn(name = "access_type_id", nullable = false)
    private Parameter accessType;

    @Column(name = "access_date", nullable = false)
    private LocalDateTime accessDate = LocalDateTime.now();
}
