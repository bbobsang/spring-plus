package org.example.expert.domain.todo.controller;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TodoControllerTest {

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    // Mockito 초기화
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화
    }

    @Test
    void todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다() {
        long invalidTodoId = 999L; // 존재하지 않는 ID

        // TodoService의 getTodo 메서드가 InvalidRequestException을 던지도록 Mock 설정
        Mockito.when(todoService.getTodo(invalidTodoId)).thenThrow(InvalidRequestException.class);

        // 예외가 발생할 것으로 예상
        assertThrows(InvalidRequestException.class, () -> {
            todoController.getTodo(invalidTodoId);
        });
    }

    // 다른 테스트들...
}
