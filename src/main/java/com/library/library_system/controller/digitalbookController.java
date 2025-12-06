package com.library.library_system.controller;

import com.library.library_system.entity.*;
import com.library.library_system.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // 1) FORMU AÇAN GET  -->  /digital/books/new
    @GetMapping("/new")
    public String showNewDigitalBookForm(Model model) {

        DigitalBook digitalBook = new DigitalBook();   // boş obje

        model.addAttribute("digitalBook", digitalBook);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("languages", bookLanguageRepository.findAll());


        return "newDigitalBook";   // src/main/resources/templates/newDigitalBook.html
    }


    @PostMapping("/save")
    public String saveDigitalBook(
            @ModelAttribute("digitalBook") DigitalBook digitalBook,
            @RequestParam("category") Integer categoryId,
            @RequestParam("author") Integer authorId,
            @RequestParam("language") Integer languageId,
            @RequestParam(value = "edition", required = false) Integer editionNumber,
            @RequestParam(value = "publisher", required = false) String publisher,
            @RequestParam("fileFormat") String fileFormat,
            @RequestParam("fileUrl") String fileUrl
            // Principal principal  ---> ŞİMDİLİK KULLANMIYORUZ, SİLEBİLİRSİN
    ) {
        System.out.println(">>> saveDigitalBook ÇALIŞTI");

        // 0) Yeni mi, edit mi?
        boolean isNew = (digitalBook.getDigitalBookId() == null);
        System.out.println("isNew = " + isNew);

        // 1) Dijital kitabı doldur
        digitalBook.setCategory(categoryRepository.findById(categoryId).orElse(null));
        digitalBook.setAuthor(authorRepository.findById(authorId).orElse(null));
        digitalBook.setLanguage(bookLanguageRepository.findById(languageId).orElse(null));
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

            // TODO: şimdilik test için worker_id = 1 kullanıyoruz
            Worker worker = workerRepository.findById(1)
                    .orElseThrow(() -> new IllegalStateException("Worker id=1 bulunamadı"));

            // DIGITAL_ACCESS_TYPE / ADD parametresi
            Parameter addType = parameterRepository
                    .findByParameterDef_NameAndValue("DIGITAL_ACCESS_TYPE", "ADD")
                    .orElseThrow(() ->
                            new IllegalStateException("DIGITAL_ACCESS_TYPE / ADD parametresi bulunamadı"));

            DigitalBookAccessLog log = new DigitalBookAccessLog();
            log.setDigitalBook(savedBook);
            log.setWorker(worker);
            log.setUser(null); // ekleme işlemini worker yaptı
            log.setAccessType(addType);
            log.setAccessDate(LocalDateTime.now());

            digitalAccessLogRepository.save(log);

            System.out.println(">>> ACCESS LOG KAYDEDİLDİ, logId henüz yok (auto-id)");
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



    @GetMapping("/delete/{id}")
    public String deleteDigitalBook(@PathVariable("id") Integer id) {
        digitalBookRepository.deleteById(id);
        return "redirect:/digital/worker/home";
    }
}


