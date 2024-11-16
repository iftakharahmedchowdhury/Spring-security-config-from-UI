package com.dev.SpringSecurity.SpringBootDB.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class pageController {
    @GetMapping("/one")
    public String one() {
        return "pageone";
    }
    @GetMapping("/two")
    public String two() {
        return "pagetwo";
    }
    @GetMapping("/three")
    public String three() {
        return "pagethree";
    }
}
