package org.example.expert.domain.comment.dto.response;

import lombok.Getter;

public class CommentResponse {

    @Getter
    private Long id;

    @Getter
    private String content;

    @Getter
    private Long todoId;


    public CommentResponse(Long id,
                           String content,
                           Long todoId) {
        this.id = id;
        this.content = content;
        this.todoId = todoId;

    }

}
