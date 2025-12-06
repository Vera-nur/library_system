package com.library.library_system.repository;

import com.library.library_system.entity.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.*;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    // Okunmamış bildirimleri getir (Tarihe göre yeni en üstte)
    List<Notification> findByUser_UserIdAndIsReadFalseOrderByCreatedAtDesc(Integer userId);

    // User ID'ye göre hepsini getir (Tarihe göre yeniden eskiye)
    List<Notification> findByUser_UserIdOrderByCreatedAtDesc(Integer userId);

    @Modifying
    @Transactional
    @Query(value = "EXEC sp_CheckDailyNotifications", nativeQuery = true)
    void runDailyCheck();

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.userId = :userId")
    void markAllAsRead(Integer userId);
}
