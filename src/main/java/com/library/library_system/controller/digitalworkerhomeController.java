package com.library.library_system.controller;

import com.library.library_system.entity.Worker;
import com.library.library_system.repository.DigitalbookRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class digitalworkerhomeController {

    private final DigitalbookRepository digitalBookRepository;

    public digitalworkerhomeController(DigitalbookRepository digitalBookRepository) {
        this.digitalBookRepository = digitalBookRepository;
    }

    @GetMapping("/digital/worker/home")
    public String showDigitalWorkerHome(Model model, HttpSession session) {

        // 🔹 Artık loggedUser değil, loggedWorker kullanıyoruz
        Worker worker = (Worker) session.getAttribute("loggedWorker");

        if (worker == null) {
            // hiç worker yoksa login sayfasına geri
            return "redirect:/";
        }

        String fullName = worker.getPerson().getName() + " " + worker.getPerson().getSurname();
        model.addAttribute("userName", fullName);

        model.addAttribute("books", digitalBookRepository.findAll());
        model.addAttribute("activePage", "digitalHome");

        return "digital-worker-home";
    }
}
