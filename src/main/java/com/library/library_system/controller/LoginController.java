package com.library.library_system.controller;

import com.library.library_system.entity.User;
import com.library.library_system.entity.Worker;
import com.library.library_system.repository.UserRepository;
import com.library.library_system.repository.WorkerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class LoginController {

    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;

    public LoginController(UserRepository userRepository,
                           WorkerRepository workerRepository) {
        this.userRepository = userRepository;
        this.workerRepository = workerRepository;
    }

    // Rol seçim sayfalarından gelen GET -> login formunu göster
    @GetMapping("/login")
    public String showLogin(@RequestParam String system,
                            @RequestParam String role,
                            Model model) {

        model.addAttribute("system", system); // digital / library
        model.addAttribute("role", role);     // user / worker

        String systemText = "digital".equals(system) ? "Dijital Sistem" : "Kütüphane Sistemi";
        String roleText = "worker".equals(role) ? "Çalışan Girişi" : "Kullanıcı Girişi";
        String idLabel = "worker".equals(role) ? "Çalışan ID" : "Kullanıcı ID";

        model.addAttribute("loginTitle", systemText + " - " + roleText);
        model.addAttribute("idLabel", idLabel);

        return "login";
    }

    // Form POST -> giriş kontrolü
    @PostMapping("/login")
    public String handleLogin(@RequestParam String system,
                              @RequestParam String role,
                              @RequestParam("loginId") Integer loginId,
                              @RequestParam String password,
                              Model model) {

        if ("user".equals(role)) {
            return handleUserLogin(system, loginId, password, model);
        } else if ("worker".equals(role)) {
            return handleWorkerLogin(system, loginId, password, model);
        } else {
            model.addAttribute("error", "Geçersiz rol.");
            return showLogin(system, role, model);
        }
    }

    // --- User login ---
    private String handleUserLogin(String system, Integer userId,
                                   String password, Model model) {

        Optional<User> optUser = userRepository.findById(userId);

        if (optUser.isEmpty()) {
            model.addAttribute("error", "Kullanıcı ID bulunamadı.");
            model.addAttribute("role", "user");
            model.addAttribute("system", system);
            return showLogin(system, "user", model);
        }

        User user = optUser.get();

        if (!user.getPerson().getPassword().equals(password)) {
            model.addAttribute("error", "Şifre hatalı.");
            model.addAttribute("role", "user");
            model.addAttribute("system", system);
            return showLogin(system, "user", model);
        }

        // TODO: burada session'a user bilgisini koyabilirsin (ileride)
        return "digital".equals(system)
                ? "redirect:/digital/dashboard"
                : "redirect:/library/dashboard";
    }

    // --- Worker login ---
    private String handleWorkerLogin(String system, Integer workerId,
                                     String password, Model model) {

        Optional<Worker> optWorker = workerRepository.findById(workerId);

        if (optWorker.isEmpty()) {
            model.addAttribute("error", "Çalışan ID bulunamadı.");
            model.addAttribute("role", "worker");
            model.addAttribute("system", system);
            return showLogin(system, "worker", model);
        }

        Worker worker = optWorker.get();

        if (!worker.getPerson().getPassword().equals(password)) {
            model.addAttribute("error", "Şifre hatalı.");
            model.addAttribute("role", "worker");
            model.addAttribute("system", system);
            return showLogin(system, "worker", model);
        }

        // TODO: burada session'a worker bilgisini koyabilirsin (ileride)
        return "digital".equals(system)
                ? "redirect:/digital/dashboard"
                : "redirect:/library/dashboard";
    }
}