package com.library.library_system.controller;

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

    // 1. ADIM: Kitap Listesinden "Ödünç Ver"e basınca burası açılır.
    // Kullanıcı seçme ekranını gösterir.
    @GetMapping("/book/{bookId}")
    public String showBorrowForm(@PathVariable Integer bookId, Model model) {
        model.addAttribute("bookId", bookId);
        model.addAttribute("users", userRepository.findAll()); // Dropdown için kullanıcıları gönder
        return "borrow-form"; // borrow-form.html dosyasını açar
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