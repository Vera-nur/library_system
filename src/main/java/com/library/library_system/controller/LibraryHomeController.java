package com.library.library_system.controller;

import com.library.library_system.entity.Notification;
import com.library.library_system.entity.ReservationView;
import com.library.library_system.repository.NotificationRepository;
import com.library.library_system.repository.ReservationViewRepository;
import com.library.library_system.service.BookService;
import com.library.library_system.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;


@Controller
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryHomeController {

    private final BookService bookService;
    private final BorrowService borrowService; // ✨ EKLENDİ
    private final NotificationRepository notificationRepository;
    private final ReservationViewRepository reservationViewRepository;

    // USER HOME
    @GetMapping("/user/home")
    public String userHome(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        String fullName = (String) session.getAttribute("fullName");
        if (userId == null) {
            // güvenlik için
            return "redirect:/login?system=library&role=user";
        }

        // 1) Tüm kitaplar (alttaki tablo)
        model.addAttribute("books", bookService.getBooksForUI());
        // Üstteki "Ödünç Aldığım Kitaplar" tablosu
        model.addAttribute("borrowList", borrowService.getBorrowsForUser(fullName));

        List<Notification> bildirimler = notificationRepository
                .findByUser_UserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        model.addAttribute("notifications", bildirimler);

        List<ReservationView> myReservations = reservationViewRepository.findByUserIdOrderByCreatedAtDesc(userId);
        model.addAttribute("myReservations", myReservations);

        return "library-user-home";
    }

    // WORKER HOME
    @GetMapping("/worker/home")
    public String workerHome(HttpSession session, Model model) {
        Integer workerId = (Integer) session.getAttribute("workerId");
        String fullName = (String) session.getAttribute("fullName");
        if (fullName == null) fullName = "Librarian";
        model.addAttribute("currentUser", fullName);
        if (workerId == null) {
            return "redirect:/login?system=library&role=worker";
        }

        // Şimdilik stats için çok basit dummy değer:
        var stats = new java.util.HashMap<String, Integer>();
        stats.put("totalBooks", bookService.getBooksForUI().size());
        stats.put("activeLoans", 0);
        stats.put("overdueBooks", 0);
        stats.put("totalMembers", 0);

        // 1) Dashboard kartları için istatistikler
        model.addAttribute("stats", borrowService.getDashboardStats());

        // 2) Alt tablodaki "Son 10 Ödünç Alma İşlemi"
        model.addAttribute("borrowList", borrowService.getLast10Borrows());

        // İstersen kitap listesini de ekleyebilirsin:
        // model.addAttribute("books", bookService.getBooksForUI());

        return "index"; // şu an kullandığın dashboard template’in ismi
    }
}
