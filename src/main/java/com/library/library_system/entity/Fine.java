package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "fines")
@Data
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fines_id")
    private Integer fineId;

    @ManyToOne
    @JoinColumn(name = "borrowbook_id", nullable = false)
    private BorrowBook borrowBook;   // FK -> borrow_books

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;       // DECIMAL(10,2) vs.

    // Örn: "PAID", "UNPAID"; ParameterDef: FINE_STATUS
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Parameter status;
}
