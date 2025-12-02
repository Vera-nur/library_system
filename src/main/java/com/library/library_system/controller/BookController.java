package com.library.library_system.controller;

import com.library.library_system.entity.Book;
import com.library.library_system.entity.BookDetailsView;
import com.library.library_system.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // --- 1. LİSTELEME (BİZİM YÖNTEM - VIEW) ---
    // Kitapları listelerken View kullanıyoruz ki Yazar Adı, Kategori Adı vb. düzgün gelsin.
    @GetMapping
    public String listBooks(Model model) {
        List<BookDetailsView> kitapListesi = bookService.getBooksForUI();
        model.addAttribute("books", kitapListesi);
        return "books"; // books.html açılır
    }

    // --- 2. YENİ KİTAP FORMU (ARKADAŞININ YÖNTEMİ) ---
    // Yeni kitap ekleme sayfasına gider
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        // İPUCU: İlerde buraya yazar ve kategori listelerini de ekleyeceğiz
        // model.addAttribute("authors", authorService.getAllAuthors());
        return "book-form"; // book-form.html açılır
    }

    // --- 3. KAYDETME İŞLEMİ (MERGE EDİLDİ) ---
    // Formdan gelen veriyi kaydeder
    @PostMapping("/save")
    public String createBook(@ModelAttribute("book") Book book) {
        bookService.saveBook(book); // Service üzerinden kayıt
        return "redirect:/books";
    }

    // --- 4. SİLME İŞLEMİ (ARKADAŞININ YÖNTEMİ + SERVICE) ---
    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Integer id) {
        bookService.deleteBookById(id); // Service üzerinden silme
        return "redirect:/books";
    }
}