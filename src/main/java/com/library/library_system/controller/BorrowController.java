package com.library.library_system.controller;

import com.library.library_system.entity.Book;
import com.library.library_system.repository.BookRepository;
import com.library.library_system.repository.UserRepository;
import com.library.library_system.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/borrows")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;
    private final UserRepository userRepository; // Kullanıcı seçtirmek için lazım
    private final BookRepository bookRepository;

    // 1. ADIM: Kitap Listesinden "Ödünç Ver"e basınca burası açılır.
    // Kullanıcı seçme ekranını gösterir.
    @GetMapping("/book/{bookId}")
    public String showBorrowForm(@PathVariable Integer bookId, Model model) {

        // 1. Kitabı bul
        Book book = bookRepository.findById(bookId).orElse(null);

        // 2. Kitap yoksa veya STOK SIFIRSA, sayfayı açma, listeye geri at
        if (book == null || book.getStock() <= 0) {
            return "redirect:/books?error=StokYetersiz";
        }

        // Her şey yolundaysa formu aç
        model.addAttribute("bookId", bookId);
        model.addAttribute("users", userRepository.findAll());
        return "borrow-form";
    }

    // 2. ADIM: Form doldurulup "Onayla" denince burası çalışır.
    @PostMapping("/create")
    public String completeBorrow(@RequestParam Integer userId, @RequestParam Integer bookId) {
        try {
            borrowService.giveBookToUser(userId, bookId);
        } catch (Exception e) {
            // Stok yoksa veya hata olursa buraya düşer
            return "redirect:/books?error=StokYok";
        }
        return "redirect:/"; // Başarılıysa Ana Sayfaya (Dashboard) git
    }
}