// CategoryRepository.java
package com.library.library_system.repository;

import com.library.library_system.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.category_name = :name")
    Optional<Category> findByCategory_name(@Param("name") String category_name);

}
