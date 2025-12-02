package com.library.library_system.service;

import com.library.library_system.entity.Book;
import com.library.library_system.entity.BookDetailsView;
import com.library.library_system.repository.BookRepository;
import com.library.library_system.repository.BookViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;          // Yazma/Silme işlemleri için (Entity)
    private final BookViewRepository bookViewRepository;  // Okuma/Listeleme işlemleri için (View)

    // 1. Listeleme (View kullanır - Hızlıdır)
    public List<BookDetailsView> getBooksForUI() {
        return bookViewRepository.findAll();
    }

    // 2. Kaydetme (Entity kullanır)
    public void saveBook(Book book) {
        // İlerde buraya iş kuralları ekleyebilirsin (Örn: Stok negatif olamaz)
        bookRepository.save(book);
    }

    // 3. Silme (Entity ID kullanır)
    public void deleteBookById(Integer id) {
        bookRepository.deleteById(id);
    }
}