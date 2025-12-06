package com.library.library_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // 1) İlk ekran: sistem seçimi (home.html)
    @GetMapping("/")
    public String home() {
        return "home";   // home.html
    }

    // 2) Dijital sistem için rol seçimi sayfası
    @GetMapping("/digital/select-role")
    public String digitalSelectRole() {
        return "digital-select-role";   // digital-select-role.html
    }

    // 3) Kütüphane sistemi için rol seçimi sayfası
    @GetMapping("/library/select-role")
    public String librarySelectRole() {
        return "library-select-role";   // library-select-role.html
    }

    // Bunlar opsiyonel, eskisi gibi durabilir
    @GetMapping("/layout-static")
    public String layoutStatic() {
        return "layout-static";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/home")
    public String showHomePage() {
        return "home"; // templates klasöründeki home.html dosyasını açar
    }

    @GetMapping("/password")
    public String password() {
        return "password";
    }

    @GetMapping("/401")
    public String error401() {
        return "401";
    }

    @GetMapping("/404")
    public String error404() {
        return "404";
    }

    @GetMapping("/500")
    public String error500() {
        return "500";
    }

    @GetMapping("/tables")
    public String tables() {
        return "tables";
    }

    @GetMapping("/charts")
    public String charts() {
        return "charts";
    }
}