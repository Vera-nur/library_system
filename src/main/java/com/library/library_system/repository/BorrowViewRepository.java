package com.library.library_system.repository;

import com.library.library_system.entity.BorrowDetailsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowViewRepository extends JpaRepository<BorrowDetailsView, Integer> {
    // Dashboard için: son 10 ödünç alma işlemi (tarihe göre tersten)
    List<BorrowDetailsView> findTop10ByOrderByStartDateDesc();

    // Dashboard'taki "Ödünçte olan kitaplar" listesi için
    // BorrowDetailsView içindeki 'status' alanına göre filtreler.
    // Örneğin: status = 'Ödünçte'
    List<BorrowDetailsView> findByStatus(String status);

    // Sol menüdeki "Ödünç İşlemleri" sayfasında
    // Kullanıcı adını (full_name) arama kutusundan bulan sorgu
    List<BorrowDetailsView> findByFullNameContainingIgnoreCase(String fullName);

    // 🔹 Kullanıcı paneli için: tam adı verilen kişinin tüm ödünçleri
    List<BorrowDetailsView> findByFullNameOrderByStartDateDesc(String fullName);





}