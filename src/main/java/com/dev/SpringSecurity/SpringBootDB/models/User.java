package com.dev.SpringSecurity.SpringBootDB.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="user_table")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String about;
    @Nullable
    private String profilePictureUrl;
    private String role;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = this.role;
        if (role!=null){
            if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role.toUpperCase();
            }
            return List.of(new SimpleGrantedAuthority(role.toUpperCase()));
        }
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
