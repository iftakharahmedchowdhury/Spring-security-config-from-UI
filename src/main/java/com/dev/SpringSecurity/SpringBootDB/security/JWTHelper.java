package com.dev.SpringSecurity.SpringBootDB.security;
import com.dev.SpringSecurity.SpringBootDB.models.AuthToken;
import com.dev.SpringSecurity.SpringBootDB.services.AuthTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;

@Component
public class JWTHelper {
    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";

    @Autowired
    private AuthTokenService authTokenService;

    //get username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //get expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // get any information from the token, we need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDateTime = now.plusSeconds(JWT_TOKEN_VALIDITY);

        AuthToken authToken = new AuthToken();
        authToken.setToken(doGenerateToken(username));
        authToken.setExpirationTime(null);
        authToken.setUserId(username);
        authTokenService.saveAuthToken(authToken);

        return authToken.getToken();
    }

    private String doGenerateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        System.out.println(!isTokenExpired(token));
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
