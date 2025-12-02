package com.library.library_system.controller;

import com.library.library_system.entity.Book;
import com.library.library_system.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // 1) Tüm kitapları listele
    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "books";   // templates/books.html
    }

    // 2) Yeni kitap formu
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        return "book-form";  // templates/book-form.html
    }

    // 3) Yeni kitabı kaydet
    @PostMapping
    public String createBook(@ModelAttribute("book") Book book) {
        // Şimdilik sadece bookName ve stock doldurulacak
        bookRepository.save(book);
        return "redirect:/books";
    }

    // 4) Kitap sil
    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Integer id) {
        bookRepository.deleteById(id);
        return "redirect:/books";
    }
}