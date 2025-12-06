// BookLanguageRepository.java
package com.library.library_system.repository;

import com.library.library_system.entity.BookLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookLanguageRepository extends JpaRepository<BookLanguage, Integer> {

    @Query("SELECT l FROM BookLanguage l WHERE l.language_name = :name")
    Optional<BookLanguage> findByLanguage_name(@Param("name") String language_name);

}
