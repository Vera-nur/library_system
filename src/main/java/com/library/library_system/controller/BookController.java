package com.library.library_system.controller;

import com.library.library_system.dto.BookFormDTO;
import com.library.library_system.entity.Book;
import com.library.library_system.entity.BookDetailsView;
import com.library.library_system.repository.*;
import com.library.library_system.service.BookService;
import com.library.library_system.service.LogService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookLanguageRepository bookLanguageRepository;
    private final LocationRepository locationRepository;
    private final BookEditionRepository bookEditionRepository;
    private final ParameterRepository parameterRepository;
    private final LogService logService;

    // --- 1. LİSTELEME (VIEW) ---
    @GetMapping
    public String listBooks(Model model) {
        List<BookDetailsView> kitapListesi = bookService.getBooksForUI();
        model.addAttribute("books", kitapListesi);
        return "books"; // books.html
    }

    // --- 2. YENİ KİTAP FORMU ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Book yerine DTO gönderiyoruz
        model.addAttribute("book", new BookFormDTO());
        return "book-form";
    }

    // --- EDİT KİTAP FORMU ---
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        BookFormDTO dto = bookService.getBookFormById(id);
        model.addAttribute("book", dto);
        return "book-form";
    }

    // --- 3. KAYDETME ---
    @PostMapping("/save")
    public String createBook(@ModelAttribute("book") BookFormDTO dto, HttpSession session) {
        // ID null ise bu yeni bir kayıttır
        boolean isNew = (dto.getBookId() == null);

        // Kitabı kaydet (Hem create hem update işlemini saveBook yapıyor varsayıyoruz)
        bookService.saveBook(dto);

        try {
            // Sadece YENİ ekleme işleminde log tutuyoruz
            if (isNew) {
                Integer workerId = (Integer) session.getAttribute("workerId");
                if (workerId != null) {
                    logService.log("create_book", null, workerId);
                }
            }
        } catch (Exception e) {
            System.out.println("Log hatası: " + e.getMessage());
        }
        return "redirect:/books";
    }

    // --- 4. SİLME (FK HATASINI YAKALA) ---
    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Integer id,
                             RedirectAttributes redirectAttributes) {

        try {
            bookService.deleteBookById(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Kitap başarıyla silindi."
            );
        } catch (DataIntegrityViolationException ex) {
            // borrow_books’ta kaydı olduğu için silemiyor
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Bu kitap ödünç işlemlerinde kullanıldığı için silinemez."
            );
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Kitap silinirken beklenmeyen bir hata oluştu."
            );
        }

        return "redirect:/books";
    }
}