package com.library.library_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

@Entity
@Immutable
@Table(name = "vw_BorrowDetails")
@Data
public class BorrowDetailsView {
    @Id
    @Column(name = "borrowbook_id")
    private Integer id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "status_label")
    private String status;
}