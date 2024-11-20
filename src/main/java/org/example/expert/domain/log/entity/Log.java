package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private String description;
    private LocalDateTime createdAt;

    public Log(String action, String description) {
        this.action = action;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }
}
