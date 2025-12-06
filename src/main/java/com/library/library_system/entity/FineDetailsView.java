package com.library.library_system.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;
import java.math.BigDecimal;

@Entity
@Immutable
@Table(name = "vw_fine_details") // SQL'deki View ismiyle aynı (küçük harf)
@Data
public class FineDetailsView {

    @Id
    @Column(name = "fines_id")
    private Integer finesId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "book_name")
    private String bookName;

    private BigDecimal amount;

    private String status; // 'paid' veya 'unpaid'
}