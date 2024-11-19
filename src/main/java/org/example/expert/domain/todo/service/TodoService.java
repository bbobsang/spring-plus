package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);

        // 날씨 정보를 가져오는 부분에서 예외처리 추가
        String weather = "Unknown"; // 기본값 설정
        try {
            weather = weatherClient.getTodayWeather();
        } catch (Exception e) {
            // 날씨 API 호출 실패 시 기본 값 사용
            // 로그 기록도 가능
        }

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail())
        );
    }

    public Page<TodoResponse> getTodos(int page, int size, String weather, LocalDateTime startDate, LocalDateTime endDate) {
        if (page < 1) {
            page = 1; // page가 1 미만이면 기본값 1로 설정
        }
        Pageable pageable = PageRequest.of(page - 1, size);

        // 날씨 조건이 있다면
        if (weather != null && !weather.isEmpty()) {
            if (startDate != null && endDate != null) {
                // 날씨와 기간 모두 있는 경우
                return todoRepository.findByWeatherAndModifiedAtBetween(weather, startDate, endDate, pageable)
                        .map(this::convertToTodoResponse);  // 변환 메소드 추가
            } else {
                // 날씨만 있는 경우
                return todoRepository.findByWeather(weather, pageable)
                        .map(this::convertToTodoResponse);  // 변환 메소드 추가
            }
        } else {
            if (startDate != null && endDate != null) {
                // 기간만 있는 경우
                return todoRepository.findByModifiedAtBetween(startDate, endDate, pageable)
                        .map(this::convertToTodoResponse);  // 변환 메소드 추가
            } else {
                // 아무 조건도 없는 경우
                return todoRepository.findAllByOrderByModifiedAtDesc(pageable)
                        .map(this::convertToTodoResponse);  // 변환 메소드 추가
            }
        }
    }

    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }

    private TodoResponse convertToTodoResponse(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }
}
