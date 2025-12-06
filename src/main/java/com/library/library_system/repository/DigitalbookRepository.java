package com.library.library_system.repository;

import com.library.library_system.entity.DigitalBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DigitalbookRepository extends JpaRepository<DigitalBook, Integer> {
}
