package com.library.library_system.service;

import com.library.library_system.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    private final NotificationRepository notificationRepository;
    private final ReservationService reservationService; // <-- YENİ EKLENDİ

    // Mevcut Görev: Her gün sabah 09:00'da bildirimleri kontrol eder
    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailyNotifications() {
        System.out.println("⏰ Daily notification check started...");
        notificationRepository.runDailyCheck();
        System.out.println("✅ Daily notifications sent successfully.");
    }

    // YENİ GÖREV: Her gece 00:00'da süresi dolan rezervasyonları temizler
    // (Böylece sabah kütüphane açıldığında stoklar güncel olur)
    @Scheduled(cron = "0 0 0 * * *")
    //"0 0 0 * * *" at 00:00
    //"0 * * * * ?" every minute
    public void cleanupExpiredReservations() {
        System.out.println("🧹 Reservation cleanup check started...");
        reservationService.checkExpiredReservations(); // Service'deki metodunu çağırır
        System.out.println("✅ Expired reservations cleaned and stocks updated.");
    }
}