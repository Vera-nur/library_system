package com.library.library_system.controller;

import com.library.library_system.entity.BorrowDetailsView;
import com.library.library_system.repository.BorrowViewRepository;
import com.library.library_system.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/library/fines") // Adresler artık /library/fines ile başlayacak
@RequiredArgsConstructor
public class FineController {

    private final BorrowViewRepository borrowViewRepository;
    private final BorrowService borrowService;

    // --- 1. SADECE GECİKMİŞ KİTAPLARI LİSTELE ---
    @GetMapping("/late-books")
    public String listLateBooks(Model model) {
        // Veritabanından durumu "Gecikmiş" olanları çekiyoruz
        List<BorrowDetailsView> lateBooks = borrowViewRepository.findByStatus("Overdue");

        // Eğer liste boşsa kullanıcıya bilgi vermek için
        if (lateBooks.isEmpty()) {
            model.addAttribute("message", "There are currently no overdue or fined books.");
        }

        model.addAttribute("lateBooks", lateBooks);
        return "late-books"; // late-books.html dosyasını açar
    }

    // --- 2. TAHSİL ET VE İADE AL (POST) ---
    @PostMapping("/pay-and-return/{borrowId}")
    public String payAndReturn(@PathVariable Integer borrowId) {
        // Servisimiz zaten (sp_ReturnBook sayesinde) hem cezayı kesiyor hem iade alıyor.
        // Biz burada "Tahsilat yapıldı" kabul edip işlemi bitiriyoruz.
        borrowService.returnBook(borrowId);

        // İşlem bitince tekrar gecikmişler listesine dön (liste azalmış olacak)
        return "redirect:/library/fines/late-books";
    }
}