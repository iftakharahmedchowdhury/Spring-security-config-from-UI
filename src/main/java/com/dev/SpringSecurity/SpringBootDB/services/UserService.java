package com.dev.SpringSecurity.SpringBootDB.services;

import com.dev.SpringSecurity.SpringBootDB.Repository.UserRepo;
import com.dev.SpringSecurity.SpringBootDB.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User>getUsers(){
        return userRepo.findAll();
    }

    public User createuser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

}
