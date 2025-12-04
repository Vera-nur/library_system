package com.library.library_system.controller;

import com.library.library_system.entity.Person;
import com.library.library_system.entity.User;
import com.library.library_system.entity.Worker;
import com.library.library_system.repository.PersonRepository;
import com.library.library_system.repository.UserRepository;
import com.library.library_system.repository.WorkerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class LoginController {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;

    public LoginController(PersonRepository personRepository,
                           UserRepository userRepository,
                           WorkerRepository workerRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        this.workerRepository = workerRepository;
    }

    // GET /login -> formu gösteriyor
    @GetMapping("/login")
    public String showLogin(@RequestParam String system,
                            @RequestParam String role,
                            Model model) {

        model.addAttribute("system", system); // digital / library
        model.addAttribute("role", role);     // user / worker

        String systemText = "digital".equals(system) ? "Dijital Sistem" : "Kütüphane Sistemi";
        String roleText = "worker".equals(role) ? "Çalışan Girişi" : "Kullanıcı Girişi";

        model.addAttribute("loginTitle", systemText + " - " + roleText);

        return "login";
    }

    // POST /login -> email + şifre ile giriş
    @PostMapping("/login")
    public String handleLogin(@RequestParam String system,
                              @RequestParam String role,   // user / worker (ekrandan seçilen)
                              @RequestParam String email,
                              @RequestParam String password,
                              Model model) {

        System.out.println(">> LOGIN TRY email=" + email + " role=" + role + " system=" + system);

        // 1) Email'e göre person bul
        Optional<Person> optPerson = personRepository.findByEmail(email);

        if (optPerson.isEmpty()) {
            model.addAttribute("error", "Bu e-posta ile kayıtlı kişi bulunamadı.");
            return showLogin(system, role, model);
        }

        Person person = optPerson.get();
        System.out.println("DB password=" + person.getPassword());

        // 2) Şifre kontrolü
        if (!person.getPassword().equals(password)) {
            model.addAttribute("error", "Şifre hatalı.");
            return showLogin(system, role, model);
        }

        // 3) Person type kontrolü (USER / WORKER)
        String personType = person.getPersonType(); // USER / WORKER
        System.out.println("personType = " + personType);

        if ("USER".equalsIgnoreCase(personType)) {

            if (!"user".equals(role)) {
                model.addAttribute("error",
                        "Bu hesap kullanıcı hesabı. Lütfen kullanıcı girişi ekranını kullanın.");
                return showLogin(system, role, model);
            }

            Optional<User> optUser = userRepository.findByPerson(person);
            if (optUser.isEmpty()) {
                model.addAttribute("error", "Bu kişi için kullanıcı kaydı bulunamadı.");
                return showLogin(system, role, model);
            }

            // TODO: Session'a user koy
            return "digital".equals(system)
                    ? "redirect:/digital/dashboard"
                    : "redirect:/library/dashboard";

        } else if ("WORKER".equalsIgnoreCase(personType)) {

            if (!"worker".equals(role)) {
                model.addAttribute("error",
                        "Bu hesap çalışan hesabı. Lütfen çalışan girişi ekranını kullanın.");
                return showLogin(system, role, model);
            }

            Optional<Worker> optWorker = workerRepository.findByPerson(person);
            if (optWorker.isEmpty()) {
                model.addAttribute("error", "Bu kişi için çalışan kaydı bulunamadı.");
                return showLogin(system, role, model);
            }

            // TODO: Session'a worker koy
            return "digital".equals(system)
                    ? "redirect:/digital/dashboard"
                    : "redirect:/library/dashboard";

        } else {
            model.addAttribute("error", "Bu kişinin tipi (person_type) geçersiz: " + personType);
            return showLogin(system, role, model);
        }
    }
}