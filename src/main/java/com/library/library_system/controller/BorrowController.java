package com.library.library_system.controller;

import com.library.library_system.entity.Book;
import com.library.library_system.repository.BookRepository;
import com.library.library_system.repository.UserRepository;
import com.library.library_system.service.BorrowService;
import com.library.library_system.repository.BorrowViewRepository;
import com.library.library_system.entity.BorrowDetailsView;
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
        return "redirect:/library/worker/home";
    }

    // --- 1. Ödünçteki kitaplar listesi ---
    @GetMapping("/list")
    public String listBorrowedBooks(Model model) {

        // Sadece ödünçte (status = "Ödünçte") olan kayıtları getir
        List<BorrowDetailsView> borrows = borrowViewRepository.findAll();

        model.addAttribute("borrows", borrows);
        return "borrow-list"; // HTML sayfası
    }

    // --- 2. Üye adı ile arama (hem form hem sonuç) ---
    // /library/borrow/search
    @GetMapping("/search")
    public String searchBorrow(
            @RequestParam(required = false) String name,
            Model model) {

        // Varsayılan: hiç sonuç yok (sayfa ilk açılış)
        java.util.List<BorrowDetailsView> borrows = null;

        if (name != null && !name.trim().isEmpty()) {
            String trimmed = name.trim();

            // İsimle eşleşen KAYITLARI çekiyoruz
            borrows = borrowViewRepository.findByFullNameContainingIgnoreCase(trimmed);

            if (borrows.isEmpty()) {
                model.addAttribute("infoMessage",
                        "“" + trimmed + "” adına ait aktif ödünç kaydı bulunamadı.");
            }

            model.addAttribute("name", trimmed);
        } else {
            model.addAttribute("name", "");
        }

        // null veya dolu olabilir – view buna göre davranacak
        model.addAttribute("borrows", borrows);

        return "borrow-search";
    }

    // --- 4. Kitabı geri alma (POST) ---
    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable Integer id) {

        // Service üzerinden kitabı teslim alıyoruz
        borrowService.returnBook(id);

        // İşlem bittikten sonra tekrar listeye dön
        return "redirect:/library/borrow/list";
    }
}