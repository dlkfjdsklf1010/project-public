package com.commerceapp.common.exception;

/**
 * 리소스를 찾을 수 없을 때 발생하는 커스텀 예외 클래스
 * RuntimeException을 상속받아 비검사 예외로 처리됩니다.
 * GlobalExceptionHandler에서 잡아 404 NOT_FOUND 응답을 반환합니다.
 * 사용 예시: 존재하지 않는 고객 ID 조회, 소프트 딜리트된 고객 조회 시
 */
public class NotFoundException extends RuntimeException{

    /**
     * @param message 예외 메시지 (클라이언트에게 반환됩니다.)
     */
    public NotFoundException(String message) {
        super(message);
    }

}
