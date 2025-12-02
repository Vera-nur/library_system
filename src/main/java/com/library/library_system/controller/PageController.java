package com.library.library_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {


    //Bu kısım için homecontroller kısmını ekledim
    //@GetMapping("/")
    //public String index() {
       // return "index"; // templates/index.html
    //}

    @GetMapping("/layout-static")
    public String layoutStatic() {
        return "layout-static";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
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