// BookFormDTO.java
package com.library.library_system.dto;

import lombok.Data;

@Data
public class BookFormDTO {

    private Integer bookId;      // düzenleme için lazım olabilir
    private String bookName;
    private Integer stock;

    private String authorName;
    private String categoryName;
    private String languageName;
    private String locationInfo;
    private String editionPublisher;
}
