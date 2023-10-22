package com.dog.shop.errorcode;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NON_LOGIN("non_login", "로그인 상태가 아닙니다."),
    EXPIRED_TOKEN("expried_token", "토큰이 만료되었습니다."),
    WRONG_TYPE_SIGNATURE("wrong_type_signature", "잘못된 서명 키입니다."),
    WRONG_ID_TOKEN("wrong_id_token", "로그아웃된 사용자의 토큰입니다."),
    INVALID_TOKEN("invaild_token", "잘못된 토큰입니다."),
    WRONG_TOKEN("wrong_token", "유효하지 않은 구성의 토큰입니다");

    private final String code;
    private String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description; // Add this line if it doesn't exist
    }
}
