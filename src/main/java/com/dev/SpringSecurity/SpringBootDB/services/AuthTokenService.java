package com.dev.SpringSecurity.SpringBootDB.services;

import com.dev.SpringSecurity.SpringBootDB.Repository.AuthTokenRepository;
import com.dev.SpringSecurity.SpringBootDB.models.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenService {
    @Autowired
    private AuthTokenRepository authTokenRepository;

    public void saveAuthToken(AuthToken authToken) {
        authTokenRepository.save(authToken);
    }

    public AuthToken findAuthTokenByUserId(String userId) {
        return authTokenRepository.findByUserId(userId);
    }

    public void updateAuthToken(AuthToken authToken) {
        authTokenRepository.save(authToken);
    }

    public AuthToken findByToken(String token) {
        return authTokenRepository.findByToken(token);
    }

    public boolean isTokenValid(AuthToken authToken) {
        // Checking token is not expired
        return authToken.getExpirationTime() == null;
    }


}
