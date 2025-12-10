package com.library.library_system.controller;

import com.library.library_system.entity.Book;
import com.library.library_system.repository.BookRepository;
import com.library.library_system.repository.UserRepository;
import com.library.library_system.service.BorrowService;
import com.library.library_system.service.LogService; // 1. IMPORT EKLE
import com.library.library_system.repository.BorrowViewRepository;
import com.library.library_system.entity.BorrowDetailsView;
import jakarta.servlet.http.HttpSession; // 2. IMPORT EKLE
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/library/borrow")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowViewRepository borrowViewRepository;
    private final BorrowService borrowService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LogService logService; // 3. BURAYA EKLE (Lombok otomatik inject eder)

    // 1. ADIM: Kitap Listesinden "Ödünç Ver"e basınca burası açılır.
    @GetMapping("/book/{bookId}")
    public String showBorrowForm(@PathVariable Integer bookId, Model model) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null || book.getStock() <= 0) {
            return "redirect:/books?error=StokYetersiz";
        }
        model.addAttribute("bookId", bookId);
        model.addAttribute("users", userRepository.findAll());
        return "borrow-form";
    }

    // 2. ADIM: Form doldurulup "Onayla" denince burası çalışır. (GÜNCELLENDİ)
    @PostMapping("/create")
    public String completeBorrow(@RequestParam Integer userId,
                                 @RequestParam Integer bookId,
                                 HttpSession session) { // 4. Session parametresi eklendi
        try {
            borrowService.giveBookToUser(userId, bookId);

            // 👇👇 LOGLAMA KISMI 👇👇
            try {
                Integer currentWorkerId = (Integer) session.getAttribute("workerId");
                // "borrow" işlemini worker yaptı, alan kişi userId
                logService.log("borrow", userId, currentWorkerId);
            } catch (Exception e) {
                System.out.println("Log hatası: " + e.getMessage());
            }
            // 👆👆 ---------------- 👆👆

        } catch (Exception e) {
            return "redirect:/books?error=StokYok";
        }
        return "redirect:/library/worker/home";
    }

    // --- 1. Ödünçteki kitaplar listesi ---
    @GetMapping("/list")
    public String listBorrowedBooks(Model model) {
        List<BorrowDetailsView> borrows = borrowViewRepository.findAll();
        model.addAttribute("borrows", borrows);
        return "borrow-list";
    }

    // --- 2. Üye adı ile arama ---
    @GetMapping("/search")
    public String searchBorrow(@RequestParam(required = false) String name, Model model) {
        List<BorrowDetailsView> borrows = null;
        if (name != null && !name.trim().isEmpty()) {
            String trimmed = name.trim();
            borrows = borrowViewRepository.findByFullNameContainingIgnoreCase(trimmed);
            if (borrows.isEmpty()) {
                model.addAttribute("infoMessage", "“" + trimmed + "” adına ait aktif ödünç kaydı bulunamadı.");
            }
            model.addAttribute("name", trimmed);
        } else {
            model.addAttribute("name", "");
        }
        model.addAttribute("borrows", borrows);
        return "borrow-search";
    }

    // --- 4. Kitabı geri alma (POST) (GÜNCELLENDİ) ---
    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable Integer id,
                             HttpSession session) { // 5. Session parametresi eklendi

        // Service üzerinden kitabı teslim alıyoruz
        borrowService.returnBook(id);

        // 👇👇 LOGLAMA KISMI 👇👇
        try {
            Integer currentWorkerId = (Integer) session.getAttribute("workerId");
            // İade işleminde elimizde direkt userId yok (sorgu atmadıkça), o yüzden User kısmına null geçiyoruz.
            // Sadece "Hangi personel iade aldı" bilgisini tutuyoruz.
            logService.log("return", null, currentWorkerId);
        } catch (Exception e) {
            System.out.println("Log hatası: " + e.getMessage());
        }
        // 👆👆 ---------------- 👆👆

        return "redirect:/library/borrow/list";
    }
}