package com.library.library_system.controller;

import com.library.library_system.entity.Person;
import com.library.library_system.entity.User;
import com.library.library_system.entity.Worker;
import com.library.library_system.repository.PersonRepository;
import com.library.library_system.repository.UserRepository;
import com.library.library_system.repository.WorkerRepository;
import jakarta.servlet.http.HttpSession;
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
                              jakarta.servlet.http.HttpSession session,
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
            // 🔹 Session'a userId yaz
            User user = optUser.get();
            // 🔹 Dijital taraf için:
            session.setAttribute("loggedUser", person);   // Person veya User, ikisinden birini kullanabilirsiniz

            session.setAttribute("userId", user.getUserId());
            String fullName = person.getName() + " " + person.getSurname();
            session.setAttribute("fullName", fullName);

            // >>> YÖNLENDİRME BURASI <<<
            if ("library".equals(system)) {
                // Kütüphane User home
                return "redirect:/library/user/home";
            } else {
                // Dijital User home
                return "redirect:/digital-user-home";
            }


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

            Worker worker = optWorker.get();
            // 🔹 Dijital taraf için:
            session.setAttribute("loggedWorker", worker);

            // 🔹 Session'a workerId yaz, Kütüphane tarafı için:
            session.setAttribute("workerId", worker.getWorkerId());

            // >>> YÖNLENDİRME BURASI <<<
            if ("library".equals(system)) {
                return "redirect:/library/worker/home";
            } else {
                // Dijital Worker home – istersen endpoint'i buna göre açarsın
                return "redirect:/digital/worker/home";
            }


        } else {
            model.addAttribute("error", "Bu kişinin tipi (person_type) geçersiz: " + personType);
            return showLogin(system, role, model);
        }
    }
}