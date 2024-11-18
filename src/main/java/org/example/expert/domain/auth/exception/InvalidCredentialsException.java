package org.example.expert.domain.auth.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    // 예외 메시지와 원인(스택 트레이스)을 모두 전달할 수 있는 생성자
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}