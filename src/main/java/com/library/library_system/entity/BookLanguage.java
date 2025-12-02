package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "book_language")
@Data
public class BookLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer language_id;

    private String language_name;
}
