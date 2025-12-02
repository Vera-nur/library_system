package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "book_edition")
@Data
public class BookEdition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "edition_id")
    private Integer editionId;

    @Column(name = "print_number", nullable = false)
    private Integer editionNumber;   // e.g., 1, 2, 3...

    @Column(name = "publisher", nullable = false)
    private String publisher;        // e.g., Penguin, HarperCollins
}