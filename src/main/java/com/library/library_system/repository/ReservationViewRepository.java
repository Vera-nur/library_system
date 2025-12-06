package com.library.library_system.repository;

import com.library.library_system.entity.ReservationView;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationViewRepository extends JpaRepository<ReservationView, Integer> {
    // Kullanıcının rezervasyonlarını getir
    List<ReservationView> findByUserIdOrderByCreatedAtDesc(Integer userId);
}