package com.commerceapp.common.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("요청 오류: " + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){
        // 어떤 필드에서 어떤 에러가 났는지 담기 위한 Map 객체를 생성합니다.
        Map<String, String> errors = new HashMap<>();
        // 발생한 모든 필드 에러를 순회하며 필드명(예: title)과 설정한 메시지(예: 10자 이내...)를 맵에 담습니다.
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );
        // 잘못된 요청임을 뜻하는 BAD_REQUEST 상태 코드와 함께 정리된 에러 상세 목록을 반환합니다.
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
