package com.library.library_system.repository;

import com.library.library_system.entity.Book; // Book entity'sini import et (Dummy dönüş için)
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReservationRepository extends JpaRepository<Book, Integer> {
    // Not: Buradaki <Book, Integer> kısmı çok önemli değil, çünkü nativeQuery kullanacağız.
    // İstersen ayrı bir Reservation entity'si oluşturup onu da bağlayabilirsin ama şimdilik SP çağıracağız.

    @Modifying
    @Transactional
    @Query(value = "EXEC sp_CreateReservation :userId, :bookId", nativeQuery = true)
    void createReservation(@Param("userId") Integer userId, @Param("bookId") Integer bookId);
}