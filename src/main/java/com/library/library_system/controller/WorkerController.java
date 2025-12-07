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

    // 🔹 0) Manage Workers Page (List + Add/Edit/Delete)
    @GetMapping("/manage")
    public String manageWorkers(@RequestParam(required = false, defaultValue = "library") String system,
                                Model model) {

        model.addAttribute("workers", workerRepository.findAll());
        model.addAttribute("systemSource", system); // geri dönüş için
        return "manageWorkers";
    }

    // 🔹 1) Yeni Worker Formu
    @GetMapping("/newWorker")
    public String showNewWorkerForm(
            @RequestParam(required = false, defaultValue = "library") String system,
            Model model) {

        Worker worker = new Worker();

        Person p = new Person();
        p.setPersonType("worker");  // otomatik worker
        worker.setPerson(p);

        model.addAttribute("worker", worker);
        model.addAttribute("systemSource", system);
        return "newWorker";
    }

    // 🔹 2) Save (Add + Edit için ortak)
    @PostMapping("/save")
    public String saveWorker(@ModelAttribute("worker") Worker worker,
                             @RequestParam("systemSource") String systemSource) {

        Person person = worker.getPerson();

        if (person != null && person.getId() != null) {
            // EDIT MODU → var olan Person güncelleniyor
            Integer personId = person.getId();

            Person existing = personRepository.findById(personId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid person id: " + personId));

            existing.setName(person.getName());
            existing.setSurname(person.getSurname());
            existing.setTel(person.getTel());
            existing.setEmail(person.getEmail());
            existing.setAddress(person.getAddress());
            existing.setPassword(person.getPassword());
            existing.setPersonType("worker");

            person = personRepository.save(existing);
        } else {
            // ADD MODU → yeni Person
            if (person == null) {
                person = new Person();
            }
            person.setPersonType("worker");
            person = personRepository.save(person);
        }

        worker.setPerson(person);
        workerRepository.save(worker);

        return "redirect:/workers/manage?system=" + systemSource;
    }

    // 🔹 3) Edit Worker (formu dolu aç)
    @GetMapping("/edit/{id}")
    public String editWorker(@PathVariable Integer id,
                             @RequestParam(required = false, defaultValue = "library") String system,
                             Model model) {

        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid worker id: " + id));

        model.addAttribute("worker", worker);
        model.addAttribute("systemSource", system);
        return "newWorker"; // aynı form hem add hem edit
    }

    // 🔹 4) Delete Worker + Person
    @PostMapping("/{id}/delete")
    public String deleteWorker(@PathVariable Integer id,
                               @RequestParam("systemSource") String systemSource) {

        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid worker id: " + id));

        Person person = worker.getPerson();
        Integer personId = (person != null ? person.getId() : null);

        // Önce worker'ı sil
        workerRepository.delete(worker);

        // Sonra bağlı person'ı sil
        if (personId != null) {
            personRepository.deleteById(personId);
        }

        return "redirect:/workers/manage?system=" + systemSource;
    }
}