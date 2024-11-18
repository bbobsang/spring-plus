package org.example.expert.domain.comment.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponse;

@Getter
public class CommentSaveResponse {

    private Long id;
    private String content;
    private Long todoId;
    private UserResponse userResponse;

    public CommentSaveResponse(Long id, String content, Long todoId, UserResponse userResponse) {
        this.id = id;
        this.content = content;
        this.todoId = todoId;
        this.userResponse = userResponse;
    }
}
