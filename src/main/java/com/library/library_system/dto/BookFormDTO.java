package com.library.library_system.dto;

import lombok.Data;

@Data
public class BookFormDTO {

    private Integer bookId;      // düzenleme için lazım olabilir
    private String bookName;
    private Integer stock;

    private Integer categoryId;
    private Integer authorId;
    private Integer languageId;
    private Integer locationId;
    private Integer editionId;
    private Integer statusId;    // Rafta / Ödünçte / Rezerve
}
