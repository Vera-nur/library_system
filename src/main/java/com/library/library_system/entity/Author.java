package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "author")
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer author_id;

    private String author_name;
}
