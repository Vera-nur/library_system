package com.library.library_system.repository;

import com.library.library_system.entity.BookDetailsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookViewRepository extends JpaRepository<BookDetailsView, Integer> {
    // Hazır! findAll() metodu zaten içinde var.
}