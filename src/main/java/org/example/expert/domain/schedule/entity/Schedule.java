package org.example.expert.domain.schedule.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.comment.entity.Comment;
import java.util.List;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToMany
    @JoinTable(name = "schedule_user",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;  // 담당자들

    private LocalDateTime createdDate; // 생성일

    @OneToMany(mappedBy = "schedule")
    private List<Comment> comments; // 댓글
}
