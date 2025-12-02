package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "notification")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;

    @Column(name = "notification_text", nullable = false, length = 500)
    private String notificationText;

    // Örn: "RESERVATION", "FINE" gibi tipler; ParameterDef: NOTIFICATION_TYPE
    @ManyToOne
    @JoinColumn(name = "notification_type_id", nullable = false)
    private Parameter notificationType;

    // Hangi objeye bağlı olduğunu tutan id (Reservation.id, Fine.id vs.)
    @Column(name = "related_entity_id", nullable = false)
    private Integer typeId;
}
