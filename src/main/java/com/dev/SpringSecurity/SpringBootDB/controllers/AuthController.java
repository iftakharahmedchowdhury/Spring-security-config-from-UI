package com.dev.SpringSecurity.SpringBootDB.controllers;

import com.dev.SpringSecurity.SpringBootDB.models.AuthToken;
import com.dev.SpringSecurity.SpringBootDB.models.JwtRequest;
import com.dev.SpringSecurity.SpringBootDB.models.JwtResponse;
import com.dev.SpringSecurity.SpringBootDB.models.User;
import com.dev.SpringSecurity.SpringBootDB.security.JWTHelper;
import com.dev.SpringSecurity.SpringBootDB.services.AuthTokenService;
import com.dev.SpringSecurity.SpringBootDB.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;


    @Autowired
    private JWTHelper helper;

    @Autowired
    private AuthTokenService authTokenService;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    @GetMapping("/create-user")
    public String create() {
        return "auth/create-user";
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        this.doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

    @PostMapping("/create-user")
    public User createUser(@RequestBody User user){
        return userService.createuser(user);
    }

   @PostMapping("/logout")
   public ResponseEntity<String> logout(HttpServletRequest request) {
       String authorizationHeader = request.getHeader("Authorization");
       if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
           String token = authorizationHeader.substring(7); // get token from "Bearer <token>"
           AuthToken authToken = authTokenService.findByToken(token);
           String username = helper.getUsernameFromToken(token);
           UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
           if (authToken != null && helper.validateToken(authToken.getToken(),userDetails) ) {
               authToken.setExpirationTime(LocalDateTime.now());
               authTokenService.updateAuthToken(authToken);
               return ResponseEntity.ok("Logged out successfully");
           }
       }
       return ResponseEntity.badRequest().body("Invalid or missing token");
   }

}
