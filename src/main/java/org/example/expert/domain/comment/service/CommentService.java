package org.example.expert.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    // 댓글 저장 메서드
    public CommentSaveResponse saveComment(AuthUser authUser, Long todoId, CommentSaveRequest commentSaveRequest) {

        // 댓글을 작성하는 유저 정보 가져오기
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 할 일 정보를 가져오기
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        // 새로운 댓글 객체 생성
        Comment comment = new Comment(commentSaveRequest.getContent(), todo, user);

        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);

        UserResponse userResponse = new UserResponse(user.getId(), user.getEmail());

        // 저장된 댓글에 대한 응답 객체 생성
        return new CommentSaveResponse(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getTodo().getId(),
                userResponse
        );
    }
}
