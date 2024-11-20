package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TodoRepositoryCustom {

    Page<Todo> applyPagination(Pageable pageable,
                               String weather,
                               LocalDateTime startDate,
                               LocalDateTime endDate);

    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    Page<Todo> findByWeather(String weather, Pageable pageable);

    Page<Todo> findByModifiedAtBetween(LocalDateTime startDate,
                                       LocalDateTime endDate,
                                       Pageable pageable);
    Page<Todo> findByWeatherAndModifiedAtBetween(String weather,
                                                 LocalDateTime startDate,
                                                 LocalDateTime endDate,
                                                 Pageable pageable);
}
