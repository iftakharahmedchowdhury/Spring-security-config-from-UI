package com.dev.SpringSecurity.SpringBootDB.Repository;

import com.dev.SpringSecurity.SpringBootDB.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,String> {

    public Optional<User> findByEmail(String Email);
}
