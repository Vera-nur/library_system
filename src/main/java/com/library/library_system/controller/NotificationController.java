package com.library.library_system.controller;

import com.library.library_system.entity.Notification;
import com.library.library_system.repository.NotificationRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/library/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    // 1. TEK BİR BİLDİRİMİ OKUNDU YAP
    @GetMapping("/mark-read/{id}")
    public String markAsRead(@PathVariable Integer id) {
        // Bildirimi bul
        Notification notification = notificationRepository.findById(id).orElse(null);

        if (notification != null) {
            notification.setIsRead(true); // Durumu güncelle
            notificationRepository.save(notification); // Kaydet
        }

        // İşlem bitince kullanıcıyı olduğu sayfaya geri at
        return "redirect:/library/user/home";
    }

    // 2. TÜMÜNÜ OKUNDU YAP
    @GetMapping("/mark-all-read")
    public String markAllAsRead(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId != null) {
            notificationRepository.markAllAsRead(userId);
        }

        return "redirect:/library/user/home";
    }

    // 3. BİLDİRİM GEÇMİŞİ SAYFASI
    @GetMapping("/history")
    public String showNotificationHistory(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        // Hepsini çekiyoruz
        List<Notification> allNotifications = notificationRepository
                .findByUser_UserIdOrderByCreatedAtDesc(userId);

        model.addAttribute("notificationList", allNotifications);

        return "notification-history"; // Yeni HTML dosyamız
    }
}