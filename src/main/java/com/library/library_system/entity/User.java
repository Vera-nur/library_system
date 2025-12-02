package com.library.library_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", nullable = false)
    private Person person;

    // Bu kullanıcının yaptığı tüm fiziksel ödünçler
    @OneToMany(mappedBy = "user")
    private List<BorrowBook> borrowBooks;

    // İlerde eklenecek:
    // Bu kullanıcının eriştiği dijital kitaplar (DigitalAccessLog veya benzeri)
    // @OneToMany(mappedBy = "user")
    // private List<DigitalAccessLog> digitalAccessLogs;

}

