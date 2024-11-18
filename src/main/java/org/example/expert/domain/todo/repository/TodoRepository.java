package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // 모든 Todo를 수정일 기준으로 내림차순으로 조회
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    // Todo ID와 User를 함께 조회
    @Query("SELECT t FROM Todo t LEFT JOIN t.user WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    // 날씨 조건으로 Todo 조회
    Page<Todo> findByWeather(String weather, Pageable pageable);

    // 수정일 기준으로 기간 내 Todo 조회
    Page<Todo> findByModifiedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // 날씨 조건과 수정일 기준 기간 내 Todo 조회
    Page<Todo> findByWeatherAndModifiedAtBetween(String weather, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
