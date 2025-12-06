package com.library.library_system.repository;

import com.library.library_system.entity.FineDetailsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FineViewRepository extends JpaRepository<FineDetailsView, Integer> {
    // Sadece ödenmemiş (unpaid) borçları getir
    List<FineDetailsView> findByStatus(String status);
}