package com.dev.SpringSecurity.SpringBootDB.controllers;

import com.dev.SpringSecurity.SpringBootDB.models.User;
import com.dev.SpringSecurity.SpringBootDB.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private UserService userService;
 @GetMapping("/homes")
 public String home() {

     return "homePage";
 }
    @GetMapping("/user")
    public List<User> getUser(){
        System.out.println("Getting users");
        return userService.getUsers();
    }
    @GetMapping("/current-user")
    public String getLoggedInUser(Principal principal){
        return principal.getName();
    }
}
