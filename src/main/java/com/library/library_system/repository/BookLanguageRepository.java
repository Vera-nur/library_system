// BookLanguageRepository.java
package com.library.library_system.repository;

import com.library.library_system.entity.BookLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLanguageRepository extends JpaRepository<BookLanguage, Integer> {
}
