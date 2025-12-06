package com.library.library_system.controller;

import com.library.library_system.entity.Person;
import com.library.library_system.entity.Worker;
import com.library.library_system.repository.PersonRepository;
import com.library.library_system.repository.WorkerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/workers")
public class WorkerController {

    private final WorkerRepository workerRepository;
    private final PersonRepository personRepository;

    public WorkerController(WorkerRepository workerRepository,
                            PersonRepository personRepository) {
        this.workerRepository = workerRepository;
        this.personRepository = personRepository;
    }

    // 📌 1) Yeni Çalışan Ekleme Formu
    @GetMapping("/newWorker")
    public String showNewWorkerForm(Model model) {

        Worker worker = new Worker();

        Person p = new Person();
        p.setPersonType("worker");  // ⬅⬅⬅ otomatik worker seçili geliyor
        worker.setPerson(p);

        model.addAttribute("worker", worker);
        return "newWorker";
    }


    // 📌 2) Çalışanı Kaydetme
    @PostMapping("/save")
    public String saveWorker(@ModelAttribute("worker") Worker worker) {

        // Person'ı önce kaydediyoruz
        Person person = worker.getPerson();
        Person savedPerson = personRepository.save(person);

        // Worker'a set ediyoruz
        worker.setPerson(savedPerson);
        workerRepository.save(worker);

        return "redirect:/digital/worker/home"; // istersen /workers/list yaparız
    }


    // 📌 3) Çalışan Düzenleme
    @GetMapping("/edit/{id}")
    public String editWorker(@PathVariable Integer id, Model model) {

        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid worker id: " + id));

        model.addAttribute("worker", worker);
        return "newWorker"; // aynı formu edit için de kullanıyoruz
    }
}
