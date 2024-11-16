package com.dev.SpringSecurity.SpringBootDB.Repository;

import com.dev.SpringSecurity.SpringBootDB.models.AuthToken;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    AuthToken findByUserId(String userId);
    AuthToken findByToken(String token);

}

