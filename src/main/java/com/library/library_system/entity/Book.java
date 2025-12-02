package com.library.library_system.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "book_name", nullable = false)
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

    @ManyToOne
    @JoinColumn(name = "edition_id")
    private BookEdition edition;

    // Fiziksel / Dijital; ParameterDef: BOOK_TYPE
    @ManyToOne
    @JoinColumn(name = "book_type_id")
    private Parameter bookType;

    // Rafta / ödünç / rezerve; ParameterDef: BOOK_STATUS
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Parameter status;
}
