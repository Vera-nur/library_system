package com.library.library_system.service;

import com.library.library_system.dto.BookFormDTO;
import com.library.library_system.entity.*;
import com.library.library_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    // -----------------------------
    // 2. KİTAP KAYDETME (ENTITY)
    // -----------------------------
    // Formdan gelen DTO'dan gerçek Book entity'si üretip kaydeder
    public void createBook(BookFormDTO dto) {

        Book book = new Book();
        book.setBookName(dto.getBookName());
        book.setStock(dto.getStock());

        if (dto.getAuthorId() != null) {
            book.setAuthor(
                    authorRepository.findById(dto.getAuthorId()).orElse(null)
            );
        }

        if (dto.getCategoryId() != null) {
            book.setCategory(
                    categoryRepository.findById(dto.getCategoryId()).orElse(null)
            );
        }

        if (dto.getLanguageId() != null) {
            book.setLanguage(
                    bookLanguageRepository.findById(dto.getLanguageId()).orElse(null)
            );
        }

        if (dto.getLocationId() != null) {
            book.setLocation(
                    locationRepository.findById(dto.getLocationId()).orElse(null)
            );
        }

        if (dto.getEditionId() != null) {
            book.setEdition(
                    bookEditionRepository.findById(dto.getEditionId()).orElse(null)
            );
        }

        if (dto.getStatusId() != null) {
            book.setStatus(
                    parameterRepository.findById(dto.getStatusId()).orElse(null)
            );
        }

        bookRepository.save(book);
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

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<BookEdition> getAllEditions() {
        return bookEditionRepository.findAll();
    }

    public List<BookLanguage> getAllLanguages() {
        return bookLanguageRepository.findAll();
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    // Ana panelde toplam kitap sayısı için
    public long countBooks() {
        return bookRepository.count();
    }
}