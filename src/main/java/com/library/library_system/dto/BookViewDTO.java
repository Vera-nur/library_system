package com.library.library_system.dto;

import lombok.Data;

@Data
public class BookViewDTO {
    private Integer id;
    private String bookName;
    private String authorName;    // Author nesnesi değil, direkt String
    private String categoryName;  // Category nesnesi değil, direkt String
    private String status;        // "Available", "Borrowed" gibi yazı
    private String type;          // "Physical", "Digital"
    private Integer stock;
}
