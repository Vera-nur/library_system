package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "digital_book")
@Data
public class DigitalBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "digital_book_id")
    private Integer digitalBookId;

    @Column(name = "file_format", nullable = false)
    private String fileFormat;   // pdf, epub, mobi...

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "digital_book_name", nullable = false)
    private String bookName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private BookLanguage language;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
