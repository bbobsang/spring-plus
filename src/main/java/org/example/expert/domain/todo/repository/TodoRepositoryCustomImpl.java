package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public TodoRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // `applyPagination` 메소드 구현
    @Override
    public Page<Todo> applyPagination(Pageable pageable, String weather, LocalDateTime startDate, LocalDateTime endDate) {
        QTodo qTodo = QTodo.todo;

        // 쿼리 작성
        JPAQuery<Todo> query = queryFactory.selectFrom(qTodo)
                .leftJoin(qTodo.user)
                .where(qTodo.weather.eq(weather)
                        .and(qTodo.modifiedAt.between(startDate, endDate)))
                .orderBy(qTodo.modifiedAt.desc());

        // 페이지네이션 적용
        List<Todo> todos = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 결과를 Page로 래핑
        return new PageImpl<>(todos, pageable, query.fetchCount());
    }

    @Override
    public Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable) {
        return applyPagination(pageable, null, null, null);
    }

    @Override
    public Page<Todo> findByWeather(String weather, Pageable pageable) {
        return applyPagination(pageable, weather, null, null);
    }

    @Override
    public Page<Todo> findByModifiedAtBetween(LocalDateTime startDate,
                                              LocalDateTime endDate,
                                              Pageable pageable) {
        return applyPagination(
                pageable,
                null,
                startDate,
                endDate);
    }

    @Override
    public Page<Todo> findByWeatherAndModifiedAtBetween(String weather,
                                                        LocalDateTime startDate,
                                                        LocalDateTime endDate,
                                                        Pageable pageable) {
        return applyPagination(
                pageable,
                weather,
                startDate,
                endDate);
    }
}
