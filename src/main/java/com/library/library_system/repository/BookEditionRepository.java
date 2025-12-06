// BookEditionRepository.java
package com.library.library_system.repository;

import com.library.library_system.entity.BookEdition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookEditionRepository extends JpaRepository<BookEdition, Integer> {
    // Dijital sistem için: editionNumber + publisher ile edition bulma
    Optional<BookEdition> findByEditionNumberAndPublisher(Integer editionNumber, String publisher);
}
