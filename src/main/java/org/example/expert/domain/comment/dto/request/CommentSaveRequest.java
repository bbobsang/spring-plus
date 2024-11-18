package org.example.expert.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentSaveRequest {

    @NotBlank(message = "Comment content is required")
    private String content;

    public CommentSaveRequest(String content) {
        this.content = content;
    }
}
