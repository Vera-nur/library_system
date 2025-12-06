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

    // Liste (istersen sonra doldurursun)
    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/digital/worker/home";
    }

    // ➕ NEW USER FORM
    @GetMapping("/newUser")
    public String showNewUserForm(Model model) {

        User user = new User();

        Person p = new Person();
        p.setPersonType("user");
        user.setPerson(p);

        model.addAttribute("user", user);
        return "newUser";
    }

    // 💾 KAYDET
    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {

        // 1) önce Person kaydet
        Person person = user.getPerson();
        // güvenlik için tekrar set edelim (biri formu kurcalarsa bile)
        person.setPersonType("user");
        Person savedPerson = personRepository.save(person);

        // 2) user'a person set et ve kaydet
        user.setPerson(savedPerson);
        userRepository.save(user);

        // sonra ister /library/user/home, ister /digital/user/home'a çevirirsin
        return "redirect:/digital/worker/home";
    }

    // ✏ EDIT (bonus)
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + id));

        model.addAttribute("user", user);
        return "newUser";   // aynı form edit için de kullanılıyor
    }
}
