package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "borrow_books") // DB’de nasıl yazdıysanız o
@Data
public class BorrowBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrowbook_id")
    private Integer borrowBookId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;      // FK -> user

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;      // FK -> book

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}
