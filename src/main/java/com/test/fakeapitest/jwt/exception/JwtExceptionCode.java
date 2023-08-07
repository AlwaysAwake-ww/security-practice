package com.test.fakeapitest.jwt.exception;


import lombok.Getter;
import lombok.Setter;


// 열거형으로 Jwt 토큰에 대한 Exception 정리
public enum JwtExceptionCode {


    // 원인을 모르는 error
    UNKNOWN_ERROR("UNKNOWN_ERROR", "UNKNOWN_ERROR"),

    // token 찾을 수 없음
    NOT_FOUND_TOKEN("NOT_FOUND_TOKEN", "Headers에 토큰 형식의 값 찾을 수 없음"),

    // 비유효 토큰
    INVALID_TOKEN("INVALID_TOKEN", "유효하지 않은 토큰"),

    // 만료된 토큰
    EXPIRED_TOKEN("EXPIRED_TOKEN", "기간이 만료된 토큰"),

    // 지원하지 않는 토큰
    UNSUPPORTED_TOKEN("UNSUPPORTED_TOKEN", "지원하지 않는 토큰");


    @Getter
    private String code;

    @Getter
    private String message;

    // 생성자
    JwtExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
