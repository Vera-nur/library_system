// AuthorRepository.java
package com.library.library_system.repository;

import com.library.library_system.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
