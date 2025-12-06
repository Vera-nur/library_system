package com.library.library_system.controller;

import com.library.library_system.entity.DigitalBookAccessLog;
import com.library.library_system.repository.DigitalAccessLogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/digital")
public class DigitalBookAccessLogController {

    private final DigitalAccessLogRepository digitalAccessLogRepository;

    // ✅ DOĞRU CONSTRUCTOR
    public DigitalBookAccessLogController(DigitalAccessLogRepository digitalAccessLogRepository) {
        this.digitalAccessLogRepository = digitalAccessLogRepository;
    }

    @GetMapping("/accesslog")
    public String showAccessLog(Model model) {

        // Eklenen kitaplar (ADD)
        List<DigitalBookAccessLog> addedLogs =
                digitalAccessLogRepository.findByAccessType_ValueOrderByAccessDateDesc("ADD");

        // İndirmeler (DOWNLOAD)
        List<DigitalBookAccessLog> downloadLogs =
                digitalAccessLogRepository.findByAccessType_ValueOrderByAccessDateDesc("DOWNLOAD");

        model.addAttribute("addedLogs", addedLogs);
        model.addAttribute("downloadLogs", downloadLogs);

        return "digital-accesslog";
    }

}
