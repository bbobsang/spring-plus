package org.example.expert.domain.todo.controller;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TodoControllerTest {

    @InjectMocks
    private TodoController todoController;

    @Mock
    private TodoService todoService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }

    @Test
    public void todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다() throws Exception {
        long todoId = 1L;

        when(todoService.getTodo(todoId))
                .thenThrow(new InvalidRequestException("Todo not found"));

        mockMvc.perform(get("/todos/" + todoId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void todo_목록_조회_시_날씨와_기간_조건_없이_조회() throws Exception {

        // Given: Todo 목록을 조회하는 경우
        Pageable pageable = PageRequest.of(0, 10);
        TodoResponse todoResponse = new TodoResponse(1L, "Todo title", "Todo contents", "Sunny", new UserResponse(1L, "email"), LocalDateTime.now(), LocalDateTime.now());
        Page<TodoResponse> todoPage = new PageImpl<>(Collections.singletonList(todoResponse), pageable, 1);

        when(todoService.getTodos(1, 10, null, null, null)).thenReturn(todoPage);

        // When: /todos 요청을 보냄
        mockMvc.perform(get("/todos")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    public void todo_목록_조회_시_날씨_조건_조회() throws Exception {

        // Given: 날씨 조건이 있는 Todo 목록 조회
        String weather = "Sunny";
        Pageable pageable = PageRequest.of(0, 10);
        TodoResponse todoResponse = new TodoResponse(1L, "Todo title", "Todo contents", weather, new UserResponse(1L, "email"), LocalDateTime.now(), LocalDateTime.now());
        Page<TodoResponse> todoPage = new PageImpl<>(Collections.singletonList(todoResponse), pageable, 1);

        when(todoService.getTodos(1, 10, weather, null, null)).thenReturn(todoPage);

        mockMvc.perform(get("/todos")
                        .param("page", "1")
                        .param("size", "10")
                        .param("weather", weather))
                .andExpect(status().isOk());
    }

    @Test
    public void todo_목록_조회_시_기간_조건_조회() throws Exception {

        // Given: 기간 조건이 있는 Todo 목록 조회
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10);
        TodoResponse todoResponse = new TodoResponse(1L, "Todo title", "Todo contents", "Sunny", new UserResponse(1L, "email"), LocalDateTime.now(), LocalDateTime.now());
        Page<TodoResponse> todoPage = new PageImpl<>(Collections.singletonList(todoResponse), pageable, 1);

        when(todoService.getTodos(1, 10, null, startDate, endDate)).thenReturn(todoPage);

        mockMvc.perform(get("/todos")
                        .param("page", "1")
                        .param("size", "10")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk());
    }
}
