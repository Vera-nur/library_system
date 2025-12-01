package com.library.library_system.controller;

import com.library.library_system.repository.PersonRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final PersonRepository personRepository;

    public TestController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/test-db")
    public String testDb() {
        try {
            long count = personRepository.count();
            return "DB OK! Kayıt sayısı: " + count;
        } catch (Exception e) {
            return "DB ERROR: " + e.getMessage();
        }
    }
}

