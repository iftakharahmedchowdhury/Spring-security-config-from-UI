package com.dev.SpringSecurity.SpringBootDB.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="Token")
@Entity
public class AuthToken {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expirationTime;

    private String userId;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}

