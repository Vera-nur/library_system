package com.library.library_system.service;

import com.library.library_system.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    private final NotificationRepository notificationRepository;

    // Her gün sabah 09:00'da çalışır
    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailyNotifications() {
        System.out.println("⏰ Daily notification check started...");
        notificationRepository.runDailyCheck();
        System.out.println("✅ Daily notifications sent successfully.");
    }
}