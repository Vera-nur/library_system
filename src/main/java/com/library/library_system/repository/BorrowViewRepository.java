package com.library.library_system.repository;

import com.library.library_system.entity.BorrowDetailsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowViewRepository extends JpaRepository<BorrowDetailsView, Integer> {
    // Son 5 işlemi getir (Tarihe göre tersten sırala)
    List<BorrowDetailsView> findTop10ByOrderByStartDateDesc();
}