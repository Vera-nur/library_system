package com.library.library_system.controller;

import com.library.library_system.entity.*;
import com.library.library_system.repository.DigitalAccessLogRepository;
import com.library.library_system.repository.DigitalbookRepository;
import com.library.library_system.repository.ParameterRepository;
import com.library.library_system.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/digital/user")
public class DigitalUserController {

    private final DigitalbookRepository digitalBookRepository;
    private final DigitalAccessLogRepository digitalAccessLogRepository;
    private final ParameterRepository parameterRepository;
    private final UserRepository userRepository;

    public DigitalUserController(DigitalbookRepository digitalBookRepository,
                                 DigitalAccessLogRepository digitalAccessLogRepository,
                                 ParameterRepository parameterRepository,
                                 UserRepository userRepository) {
        this.digitalBookRepository = digitalBookRepository;
        this.digitalAccessLogRepository = digitalAccessLogRepository;
        this.parameterRepository = parameterRepository;
        this.userRepository = userRepository;
    }

    // Kullanıcının kitap listesi ekranı
    @GetMapping("/home")
    public String userHome(Model model, HttpSession session) {

        model.addAttribute("books", digitalBookRepository.findAll());
        model.addAttribute("activePage", "digitalHome");

        Object obj = session.getAttribute("loggedUser");

        if (obj instanceof User user) {
            model.addAttribute("loggedInName",
                    user.getPerson().getName() + " " + user.getPerson().getSurname());
        } else if (obj instanceof Person person) {
            // Şu an senin sistemin Person tutuyor, o yüzden burası çalışacak
            model.addAttribute("loggedInName",
                    person.getName() + " " + person.getSurname());
        } else {
            model.addAttribute("loggedInName", "User");
        }

        return "digital-user-home";
    }

    // Open: URL'e yönlendir + log kaydet
    @GetMapping("/open/{id}")
    public String openDigitalBook(@PathVariable Integer id, HttpSession session) {

        DigitalBook book = digitalBookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Digital book not found: " + id));

        String url = book.getFileUrl();
        if (url == null || url.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "This digital book has no file URL");
        }

        Object obj = session.getAttribute("loggedUser");
        User user = null;

        if (obj instanceof User u) {
            user = u;
        } else if (obj instanceof Person p) {
            user = userRepository.findByPerson(p)
                    .orElse(null);
        }

        if (user != null) {
            Parameter downloadType = parameterRepository
                    .findByParameterDef_NameAndValue("DIGITAL_ACCESS_TYPE", "DOWNLOAD")
                    .orElseThrow(() -> new IllegalStateException(
                            "DIGITAL_ACCESS_TYPE / DOWNLOAD parametresi bulunamadı"));

            DigitalBookAccessLog log = new DigitalBookAccessLog();
            log.setDigitalBook(book);
            log.setUser(user);
            log.setWorker(null);
            log.setAccessType(downloadType);
            log.setAccessDate(LocalDateTime.now());

            digitalAccessLogRepository.save(log);
        }

        return "redirect:" + url;
    }

    // Kullanıcının indirdiği kitaplar
    @GetMapping("/downloads")
    public String userDownloads(Model model, HttpSession session) {

        Object obj = session.getAttribute("loggedUser");
        if (obj == null) {
            throw new IllegalStateException("Oturumda kullanıcı yok (loggedUser).");
        }

        User user;
        if (obj instanceof User u) {
            user = u;
        } else if (obj instanceof Person p) {
            user = userRepository.findByPerson(p)
                    .orElseThrow(() -> new IllegalStateException(
                            "Bu kişiye bağlı User kaydı bulunamadı."));
        } else {
            throw new IllegalStateException("loggedUser tipi beklenmeyen bir tip.");
        }

        model.addAttribute("logs",
                digitalAccessLogRepository.findByUserOrderByAccessDateDesc(user));

        model.addAttribute("activePage", "downloads");
        model.addAttribute("loggedInName",
                user.getPerson().getName() + " " + user.getPerson().getSurname());

        return "digital-user-downloads";
    }
}
