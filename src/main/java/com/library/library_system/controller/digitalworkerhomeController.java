package com.library.library_system.controller;

import com.library.library_system.entity.Person;
import com.library.library_system.repository.DigitalbookRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

@Controller
public class digitalworkerhomeController {

    private final DigitalbookRepository digitalBookRepository;

    public digitalworkerhomeController(DigitalbookRepository digitalBookRepository) {
        this.digitalBookRepository = digitalBookRepository;
    }
//    @GetMapping("/digital/accesslog")
//    public String showDigitalAccessLog() {
//        return "digital-accesslog"; // digital-accesslog.html
//    }

    @GetMapping("/digital/worker/home")
    public String showDigitalWorkerHome(Model model, HttpSession session) {
        Person user = (Person) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("userName", user.getName());

        // 🔥 ÖNEMLİ: her zaman boş liste bile olsa set et
        model.addAttribute("books", digitalBookRepository.findAll());

        return "digital-worker-home";
    }
}


