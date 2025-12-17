package com.library.library_system.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.library.library_system.entity.DigitalBookAccessLog;
import com.library.library_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DigitalAccessLogRepository
        extends JpaRepository<DigitalBookAccessLog, Integer> {

    List<DigitalBookAccessLog>
    findByAccessType_ValueOrderByAccessDateDesc(String value);
    List<DigitalBookAccessLog> findByUserOrderByAccessDateDesc(User user);

    @Modifying
    @Transactional
    @Query("delete from DigitalBookAccessLog l where l.digitalBook.digitalBookId = :bookId")
    void deleteAllByDigitalBookId(@Param("bookId") Integer bookId);

}
