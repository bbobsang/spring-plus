package org.example.expert.domain.schedule.dto.response;

import lombok.Getter;

@Getter
public class ScheduleSearchResponse {

    private String title; // 일정 제목
    private Long userCount; // 담당자 수
    private Long commentCount; // 댓글 수

    public ScheduleSearchResponse(String title, Long userCount, Long commentCount) {
        this.title = title;
        this.userCount = userCount;
        this.commentCount = commentCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }
}

