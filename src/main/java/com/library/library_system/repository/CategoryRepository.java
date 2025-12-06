// CategoryRepository.java
package com.library.library_system.repository;

import com.library.library_system.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
