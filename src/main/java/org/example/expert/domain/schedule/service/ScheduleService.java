package org.example.expert.domain.schedule.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.schedule.dto.response.ScheduleSearchResponse;
import org.example.expert.domain.schedule.entity.QSchedule;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ScheduleService {

    private final JPAQueryFactory queryFactory;

    // 생성자 주입
    public ScheduleService(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Page<ScheduleSearchResponse> searchSchedules(
            String title,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String nickname,
            Pageable pageable) {

        QSchedule schedule = QSchedule.schedule;
        QUser user = QUser.user;

        // 기본 쿼리 조건을 저장할 BooleanBuilder
        BooleanBuilder where = new BooleanBuilder();

        // 제목 조건 (부분 일치)
        if (title != null && !title.isEmpty()) {
            where.and(schedule.title.containsIgnoreCase(title));
        }

        // 생성일 범위 조건
        if (startDate != null && endDate != null) {
            where.and(schedule.createdDate.between(startDate, endDate));
        }

        // 담당자 닉네임 조건 (부분 일치)
        if (nickname != null && !nickname.isEmpty()) {
            where.and(user.nickname.containsIgnoreCase(nickname));
        }

        // QueryDSL을 이용하여 원하는 정보만 조회
        List<ScheduleSearchResponse> results = queryFactory
                .select(Projections.constructor(ScheduleSearchResponse.class,
                        schedule.title,
                        schedule.users.size(),
                        schedule.comments.size()
                ))
                .from(schedule)
                .leftJoin(schedule.users, user)
                .leftJoin(schedule.comments)
                .where(where)
                .orderBy(schedule.createdDate.desc()) // 최신순 정렬
                .offset(pageable.getOffset()) // 페이지 시작점
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch(); // 결과를 가져옴

        // 전체 결과의 수를 구하기 위한 쿼리
        Long total = queryFactory
                .select(schedule.count())
                .from(schedule)
                .leftJoin(schedule.users, user)
                .leftJoin(schedule.comments)
                .where(where)
                .fetchOne();

        // null을 방지하고 기본값 0을 반환
        total = Optional.ofNullable(total).orElse(0L);

        return new PageImpl<>(results, pageable, total);
    }
}
