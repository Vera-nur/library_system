// LocationRepository.java
package com.library.library_system.repository;

import com.library.library_system.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query("SELECT l FROM Location l WHERE l.location_info = :info")
    Optional<Location> findByLocation_info(@Param("info") String locationInfo);
}
