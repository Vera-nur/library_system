package com.library.library_system.controller;

import com.library.library_system.entity.Person;
import com.library.library_system.entity.User;
import com.library.library_system.repository.PersonRepository;
import com.library.library_system.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")   // /users/...
public class UserController {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;

    public UserController(UserRepository userRepository,
                          PersonRepository personRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
    }

    // 🔹 1) Manage Users Page (Listeleme)
    @GetMapping("/manage")
    public String manageUsers(@RequestParam(required = false, defaultValue = "library") String system,
                              Model model) {

        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("systemSource", system);   // library / digital
        return "manageUsers";
    }

    // 🔹 2) Yeni User Formu
    @GetMapping("/newUser")
    public String showNewUserForm(
            @RequestParam(required = false, defaultValue = "library") String system,
            Model model) {

        User user = new User();

        Person p = new Person();
        p.setPersonType("user");   // otomatik user
        user.setPerson(p);

        model.addAttribute("user", user);
        model.addAttribute("systemSource", system);
        return "newUser";
    }

    // 🔹 3) Kaydet (Hem Add hem Edit için)
    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam("systemSource") String systemSource) {

        Person person = user.getPerson();

        if (person.getId() != null) {
            // EDIT MODU: Var olan person güncelleniyor
            Integer personId = person.getId();

            Person existing = personRepository.findById(personId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid person id: " + personId));

            existing.setName(person.getName());
            existing.setSurname(person.getSurname());
            existing.setTel(person.getTel());
            existing.setEmail(person.getEmail());
            existing.setAddress(person.getAddress());
            existing.setPassword(person.getPassword());
            existing.setPersonType("user");

            person = personRepository.save(existing);
        } else {
            // ADD MODU: Yeni person oluştur
            person.setPersonType("user");
            person = personRepository.save(person);
        }

        // User ile ilişkilendir ve kaydet
        user.setPerson(person);
        userRepository.save(user);

        // Tek exit point → hem library hem digital için
        return "redirect:/users/manage?system=" + systemSource;
    }

    // 🔹 4) Edit User (formu dolu aç)
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Integer id,
                           @RequestParam(required = false, defaultValue = "library") String system,
                           Model model) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + id));

        model.addAttribute("user", user);
        model.addAttribute("systemSource", system);
        return "newUser";   // aynı form edit için de kullanılıyor
    }

    // 🔹 5) Delete User + Person
    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Integer id,
                             @RequestParam("systemSource") String systemSource) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + id));

        Person person = user.getPerson();
        Integer personId = (person != null ? person.getId() : null);

        // Önce user'ı sil
        userRepository.delete(user);

        // Sonra bağlı person'ı sil
        if (personId != null) {
            personRepository.deleteById(personId);
        }

        return "redirect:/users/manage?system=" + systemSource;
    }

    // (İstersen bunu tamamen silebilirsin, manage sayfası varken çok gerek yok)
    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/digital/worker/home";
    }
}