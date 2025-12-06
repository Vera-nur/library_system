package com.library.library_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "vw_my_reservation")
@Data
public class ReservationView {
    @Id
    private Integer reservationId;
    private Integer userId;
    private String bookName;

    private String typeCode; // HOLD (CSS class için)
    private String typeDesc; // Ekranda görünecek yazı

    // DURUM BİLGİLERİ
    private String statusCode; // RES_ACTIVE (CSS class için)
    private String statusDesc; // Ekranda görünecek yazı

    private LocalDateTime createdAt;
    private LocalDateTime expirationDate;
}