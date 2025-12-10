package com.library.library_system.repository;

import com.library.library_system.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessLogRepository extends JpaRepository<AccessLog, Integer> {
    // Özel bir metoda gerek yok, save() yeterli.
    List<AccessLog> findAllByOrderByCreatedAtDesc();
}