// LocationRepository.java
package com.library.library_system.repository;

import com.library.library_system.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
