package com.dev.SpringSecurity.SpringBootDB.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WelcomePageController {
    @GetMapping()
    public String home() {
        return "welcome";
    }
}


