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

    // Her dijital kitap bir fiziksel kitaba bağlıdır
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "file_format", nullable = false)
    private String fileFormat;   // pdf, epub, mobi...

    @Column(name = "page_count")
    private Integer pageCount;
}
