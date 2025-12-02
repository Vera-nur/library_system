package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable // Veri değiştirilemez demek
@Table(name = "vw_BookDetails") // SQL'deki View ismiyle AYNI olmalı
@Data
public class BookDetailsView {
    @Id
    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "book_name")
    private String bookName;

    private Integer stock;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "status_value")
    private String status;

    @Column(name = "type_value")
    private String type;
}
