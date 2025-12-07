package com.library.library_system.service;

import com.library.library_system.dto.BookFormDTO;
import com.library.library_system.entity.*;
import com.library.library_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookViewRepository bookViewRepository;

    // EKLENEN REPO'lar
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookEditionRepository bookEditionRepository;
    private final BookLanguageRepository bookLanguageRepository;
    private final LocationRepository locationRepository;
    private final ParameterRepository parameterRepository;


    // -----------------------------
    // 1. KİTAP LİSTESİ (VIEW)
    // -----------------------------
    public List<BookDetailsView> getBooksForUI() {
        return bookViewRepository.findAll();
    }

    // 2) CREATE + UPDATE
    @Transactional
    public void saveBook(BookFormDTO dto) {

        // --- Mevcut kitap mı, yeni mi? ---
        Book book;
        if (dto.getBookId() != null) {
            book = bookRepository.findById(dto.getBookId())
                    .orElseThrow(() -> new IllegalArgumentException("Book not found, id=" + dto.getBookId()));
        } else {
            book = new Book();
        }

        // --- Textleri temizle ---
        String authorName   = safeTrim(dto.getAuthorName());
        String categoryName = safeTrim(dto.getCategoryName());
        String languageName = safeTrim(dto.getLanguageName());
        String locationInfo = safeTrim(dto.getLocationInfo());
        String publisher    = safeTrim(dto.getEditionPublisher());

        // --- Author ---
        Author author = authorRepository
                .findByAuthor_name(authorName)
                .orElseGet(() -> {
                    Author a = new Author();
                    a.setAuthor_name(authorName);
                    return authorRepository.save(a);
                });

        // --- Category ---
        Category category = categoryRepository
                .findByCategory_name(categoryName)
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setCategory_name(categoryName);
                    return categoryRepository.save(c);
                });

        // --- Language ---
        BookLanguage language = bookLanguageRepository
                .findByLanguage_name(languageName)
                .orElseGet(() -> {
                    BookLanguage l = new BookLanguage();
                    l.setLanguage_name(languageName);
                    return bookLanguageRepository.save(l);
                });

        // --- Location ---
        Location location = locationRepository
                .findByLocation_info(locationInfo)
                .orElseGet(() -> {
                    Location loc = new Location();
                    loc.setLocation_info(locationInfo);
                    loc.setIsLocation(true);
                    return locationRepository.save(loc);
                });

        // --- Edition ---
        BookEdition edition = bookEditionRepository
                .findByPublisher(publisher)
                .orElseGet(() -> {
                    BookEdition e = new BookEdition();
                    e.setPublisher(publisher);
                    e.setEditionNumber(1);
                    return bookEditionRepository.save(e);
                });

        // --- Status (stok'a göre) ---
        Parameter status = resolveStatusByStock(dto.getStock());

        // --- Book alanlarını güncelle ---
        book.setBookName(dto.getBookName());
        book.setStock(dto.getStock());
        book.setAuthor(author);
        book.setCategory(category);
        book.setLanguage(language);
        book.setLocation(location);
        book.setEdition(edition);
        book.setStatus(status);

        bookRepository.save(book);
    }

    // 3) Edit formuna dto hazırlayan metot
    public BookFormDTO getBookFormById(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found, id=" + id));

        BookFormDTO dto = new BookFormDTO();
        dto.setBookId(book.getBookId());
        dto.setBookName(book.getBookName());
        dto.setStock(book.getStock());

        dto.setAuthorName(
                book.getAuthor() != null ? book.getAuthor().getAuthor_name() : null
        );
        dto.setCategoryName(
                book.getCategory() != null ? book.getCategory().getCategory_name() : null
        );
        dto.setLanguageName(
                book.getLanguage() != null ? book.getLanguage().getLanguage_name() : null
        );
        dto.setLocationInfo(
                book.getLocation() != null ? book.getLocation().getLocation_info() : null
        );
        dto.setEditionPublisher(
                book.getEdition() != null ? book.getEdition().getPublisher() : null
        );

        return dto;
    }


    // -----------------------------
    // 3. KİTAP SİLME
    // -----------------------------
    public void deleteBookById(Integer id) {
        bookRepository.deleteById(id);
    }


    // -----------------------------
    // 4. DROP-DOWN LİSTELERİ
    // -----------------------------

    // Ana panelde toplam kitap sayısı için
    public long countBooks() {
        return bookRepository.count();
    }

    private Parameter resolveStatusByStock(Integer stock) {
        final String DEF_NAME = "BOOK_STATUS";  // ParameterDef.name

        String value;
        if (stock == null || stock == 0) {
            value = "OUT_OF_STOCK";
        } else {
            value = "AVAILABLE";
        }

        return parameterRepository
                .findByParameterDef_NameAndValue(DEF_NAME, value)
                .orElseThrow(() ->
                        new IllegalStateException("Parametre bulunamadı: " + DEF_NAME + " / " + value));
    }

    private String safeTrim(String s) {
        return (s == null) ? null : s.trim();
    }
}