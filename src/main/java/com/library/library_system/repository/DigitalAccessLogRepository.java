package com.library.library_system.repository;

import com.library.library_system.entity.DigitalBookAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DigitalAccessLogRepository
        extends JpaRepository<DigitalBookAccessLog, Integer> {

    List<DigitalBookAccessLog>
    findByAccessType_ValueOrderByAccessDateDesc(String value);
}
