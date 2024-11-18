package org.example.expert.domain.common.exception;

public class InvalidRequestException extends RuntimeException {
    private int code;
    private String status;

    // 기존 생성자: 코드, 상태, 메시지를 받는 생성자
    public InvalidRequestException(int code, String status, String message) {
        super(message);
        this.code = code;
        this.status = status;
    }

    // 코드와 메시지만 받는 생성자 (기본값으로 코드=400, 상태="BAD_REQUEST")
    public InvalidRequestException(String message) {
        super(message);
        this.code = 400;  // 기본 오류 코드: 400 (BAD_REQUEST)
        this.status = "BAD_REQUEST";
    }


    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
