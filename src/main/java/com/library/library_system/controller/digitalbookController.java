package com.library.library_system.controller;


import com.library.library_system.entity.*;
import com.library.library_system.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;


import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/digital/books")
public class digitalbookController {

    private final DigitalbookRepository digitalBookRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final BookLanguageRepository bookLanguageRepository;
    private final BookEditionRepository bookEditionRepository;

    private final PersonRepository personRepository;
    private final WorkerRepository workerRepository;
    private final ParameterRepository parameterRepository;
    private final DigitalAccessLogRepository digitalAccessLogRepository;


    public digitalbookController(DigitalbookRepository digitalBookRepository,
                                 CategoryRepository categoryRepository,
                                 AuthorRepository authorRepository,
                                 BookLanguageRepository bookLanguageRepository,
                                 BookEditionRepository bookEditionRepository,
                                 PersonRepository personRepository,
                                 WorkerRepository workerRepository,
                                 ParameterRepository parameterRepository,
                                 DigitalAccessLogRepository digitalAccessLogRepository) {
        this.digitalBookRepository = digitalBookRepository;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
        this.bookLanguageRepository = bookLanguageRepository;
        this.bookEditionRepository = bookEditionRepository;
        this.personRepository=personRepository;
        this.parameterRepository=parameterRepository;
        this.workerRepository=workerRepository;
        this.digitalAccessLogRepository=digitalAccessLogRepository;
    }

    @PostMapping("/save")
    public String saveDigitalBook(
            @ModelAttribute("digitalBook") DigitalBook digitalBook,
            @RequestParam("author_name") String author_name,
            @RequestParam("category_name") String category_name,
            @RequestParam("language_name") String language_name,
            @RequestParam(value = "edition", required = false) Integer editionNumber,
            @RequestParam(value = "publisher", required = false) String publisher,
            @RequestParam("fileFormat") String fileFormat,
            @RequestParam("fileUrl") String fileUrl,
            HttpSession session
    ) {
        System.out.println(">>> saveDigitalBook ÇALIŞTI");

        // 0) Yeni mi, edit mi?
        boolean isNew = (digitalBook.getDigitalBookId() == null
                || digitalBook.getDigitalBookId() == 0);

        System.out.println("isNew = " + isNew);

        Category category = getOrCreateCategory(category_name);
        Author author = getOrCreateAuthor(author_name);
        BookLanguage language = getOrCreateLanguage(language_name);

        // 1) Dijital kitabı doldur
        digitalBook.setCategory(category);
        digitalBook.setAuthor(author);
        digitalBook.setLanguage(language);
        digitalBook.setFileFormat(fileFormat);
        digitalBook.setFileUrl(fileUrl);

        if (editionNumber != null && publisher != null && !publisher.isBlank()) {
            BookEdition edition = new BookEdition();
            edition.setEditionNumber(editionNumber);
            edition.setPublisher(publisher);
            BookEdition savedEdition = bookEditionRepository.save(edition);
            digitalBook.setEdition(savedEdition);
        } else {
            digitalBook.setEdition(null);
        }

        // 2) Kitabı kaydet
        DigitalBook savedBook = digitalBookRepository.save(digitalBook);

        // 3) SADECE YENİ KAYITTA log yaz (test için sabit worker + sabit parametre)
        if (isNew) {
            System.out.println(">>> ACCESS LOG BLOĞUNA GİRİYORUM");

            Worker worker = (Worker) session.getAttribute("loggedWorker");
            if (worker == null) {
                throw new IllegalStateException("Worker session not found! Please log in as a worker first.");
            }


            // DIGITAL_ACCESS_TYPE / ADD parametresi
            Parameter addType = parameterRepository
                    .findByParameterDef_NameAndValue("DIGITAL_ACCESS_TYPE", "ADD")
                    .orElseThrow(() ->
                            new IllegalStateException("DIGITAL_ACCESS_TYPE /ADD parameter not found."));

            DigitalBookAccessLog log = new DigitalBookAccessLog();
            log.setDigitalBook(savedBook);
            log.setWorker(worker);
            log.setUser(null); // ekleme işlemini worker yaptı
            log.setAccessType(addType);
            log.setAccessDate(LocalDateTime.now());

            digitalAccessLogRepository.save(log);

            System.out.println(">>> Access log recorded, log ID not yet available (auto-id)");
        }

        return "redirect:/digital/worker/home";
    }



    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        DigitalBook digitalBook = digitalBookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + id));

        model.addAttribute("digitalBook", digitalBook);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("languages", bookLanguageRepository.findAll());

        return "newDigitalBook";
    }

    @GetMapping("/new")
    public String showNewDigitalBookForm(Model model) {
        DigitalBook digitalBook = new DigitalBook();

        model.addAttribute("digitalBook", digitalBook);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("languages", bookLanguageRepository.findAll());

        return "newDigitalBook"; // templates/newDigitalBook.html
    }




    @GetMapping("/delete/{id}")
    @Transactional
    public String deleteDigitalBook(@PathVariable("id") Integer id) {
        // ✅ 1) Önce o kitaba bağlı tüm logları sil
        digitalAccessLogRepository.deleteAllByDigitalBookId(id);

        // ✅ 2) Sonra kitabı sil
        digitalBookRepository.deleteById(id);
        return "redirect:/digital/worker/home";
    }

    private Category getOrCreateCategory(String name) {
        String trimmed = name == null ? null : name.trim();
        if (trimmed == null || trimmed.isEmpty()) {
            throw new IllegalArgumentException("Category name can not be empty");
        }

        return categoryRepository.findByCategory_name(trimmed)
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setCategory_name(trimmed);
                    return categoryRepository.save(c);
                });
    }

    private Author getOrCreateAuthor(String name) {
        String trimmed = name == null ? null : name.trim();
        if (trimmed == null || trimmed.isEmpty()) {
            throw new IllegalArgumentException("Author name can not be empty");
        }

        return authorRepository.findByAuthor_name(trimmed)
                .orElseGet(() -> {
                    Author a = new Author();
                    a.setAuthor_name(trimmed);
                    return authorRepository.save(a);
                });
    }

    private BookLanguage getOrCreateLanguage(String name) {
        String trimmed = name == null ? null : name.trim();
        if (trimmed == null || trimmed.isEmpty()) {
            throw new IllegalArgumentException("Language name can not be empty");
        }

        return bookLanguageRepository.findByLanguage_name(trimmed)
                .orElseGet(() -> {
                    BookLanguage l = new BookLanguage();
                    l.setLanguage_name(trimmed);
                    return bookLanguageRepository.save(l);
                });
    }


}


