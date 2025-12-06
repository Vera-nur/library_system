package com.library.library_system.controller;

import com.library.library_system.dto.BookFormDTO;
import com.library.library_system.entity.Book;
import com.library.library_system.entity.BookDetailsView;
import com.library.library_system.repository.*;
import com.library.library_system.service.BookService;
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

        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("languages", bookLanguageRepository.findAll());
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("editions", bookEditionRepository.findAll());

        model.addAttribute("statuses",
                parameterRepository.findByParameterDef_Id(1));

        return "book-form";
    }

    // --- 3. KAYDETME ---
    @PostMapping("/save")
    public String createBook(@ModelAttribute("book") BookFormDTO dto) {
        bookService.createBook(dto);   // birazdan yazacağız
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