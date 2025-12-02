package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "reservation")
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer reservation_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;          // FK -> user

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;          // FK -> book
}
