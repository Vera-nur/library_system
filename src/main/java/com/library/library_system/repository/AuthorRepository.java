// AuthorRepository.java
package com.library.library_system.repository;

import com.library.library_system.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    @Query("SELECT a FROM Author a WHERE a.author_name = :name")
    Optional<Author> findByAuthor_name(@Param("name") String author_name);
}
