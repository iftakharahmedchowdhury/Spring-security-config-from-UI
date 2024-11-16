package com.dev.SpringSecurity.SpringBootDB.services;

import com.dev.SpringSecurity.SpringBootDB.Repository.UserRepo;
import com.dev.SpringSecurity.SpringBootDB.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private UserRepo userRepo;

    public CustomUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =userRepo.findByEmail(username).orElseThrow(()-> new RuntimeException("User not found"));
        return user;
    }
}
