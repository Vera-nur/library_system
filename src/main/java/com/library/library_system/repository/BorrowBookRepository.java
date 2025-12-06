package com.library.library_system.repository;

import com.library.library_system.dto.DashboardStats; // DTO paketini kontrol et
import com.library.library_system.entity.BorrowBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // EKLENDİ
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // EKLENDİ
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional; // EKLENDİ

@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook, Integer> {

    // --- 1. DASHBOARD İSTATİSTİKLERİ (ZATEN VARDI) ---
    @Query(value = "EXEC sp_GetDashboardStats", nativeQuery = true)
    DashboardStats getDashboardStatistics();

    // --- 2. ÖDÜNÇ VERME İŞLEMİ (YENİ EKLENECEK KISIM) ---
    // Bu metod Java'dan SQL'deki 'sp_BorrowBook' prosedürünü çağırır.
    // Prosedür hem kaydı ekler hem de stoğu 1 azaltır.
    @Modifying
    @Transactional
    @Query(value = "EXEC sp_BorrowBook :userId, :bookId", nativeQuery = true)
    void borrowBook(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    // Kitabı geri alma (stok +1)
    @Modifying
    @Transactional
    @Query(value = "EXEC sp_ReturnBook :borrowId", nativeQuery = true)
    void returnBook(@Param("borrowId") Integer borrowId);

}