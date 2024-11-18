package org.example.expert.domain.comment.repository;

import org.example.expert.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c " +
            "JOIN FETCH c.todo t " +   // Todo 엔티티를 fetch join
            "JOIN FETCH c.user u " +   // User 엔티티를 fetch join
            "WHERE c.todo.id = :todoId")
    List<Comment> findAllByTodoId(@Param("todoId") Long todoId);
}
