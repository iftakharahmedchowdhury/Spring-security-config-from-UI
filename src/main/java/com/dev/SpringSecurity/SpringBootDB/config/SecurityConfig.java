package com.dev.SpringSecurity.SpringBootDB.config;

import com.dev.SpringSecurity.SpringBootDB.models.PermissionUrl;
import com.dev.SpringSecurity.SpringBootDB.security.JWTAthenticationEntryPoint;
import com.dev.SpringSecurity.SpringBootDB.security.JWTAuthenticationFilter;
import com.dev.SpringSecurity.SpringBootDB.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Autowired
    private JWTAthenticationEntryPoint point;
    @Autowired
    private JWTAuthenticationFilter filter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PermissionService permissionService;

    public SecurityConfig(JWTAthenticationEntryPoint point, JWTAuthenticationFilter filter, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, PermissionService permissionService) {
        this.point = point;
        this.filter = filter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.permissionService = permissionService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        List<PermissionUrl> permissions = permissionService.getPermissions();

        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> {

                    auth.requestMatchers("/", "/auth/login", "/auth/create-user", "/api/**", "/permissions/**").permitAll();


                    // Default authenticated URL rules
                    auth.requestMatchers("/home/homes").authenticated()
                            .requestMatchers("/home/**").authenticated()
                            .requestMatchers("/auth/logout").authenticated()
                            .anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
