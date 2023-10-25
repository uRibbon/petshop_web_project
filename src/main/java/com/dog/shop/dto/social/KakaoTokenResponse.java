package com.dog.shop.dto.social;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class KakaoTokenResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("kakao_account")
    private Map<String, Object> kakao_account;

    @JsonProperty("properties")
    private Map<String, Object> properties;

    // 이메일을 반환하는 메소드를 추가합니다.
    public String getEmail() {
        if (kakao_account != null && kakao_account.containsKey("email")) {
            return (String) kakao_account.get("email");
        }
        return null;
    }

    public String getName() {
        if (kakao_account != null && kakao_account.containsKey("name")) {
            return (String) kakao_account.get("name");
        }
        return null;
    }

    public String getBirthday() {
        if (kakao_account != null && kakao_account.containsKey("birthday")) {
            return (String) kakao_account.get("birthday");
        }
        return null;
    }

    public String getBirthyear() {
        if (kakao_account != null && kakao_account.containsKey("birthyear")) {
            return (String) kakao_account.get("birthyear");
        }
        return null;
    }

    public String getPhoneNumber() {
        if (kakao_account != null && kakao_account.containsKey("phone_number")) {
            return (String) kakao_account.get("phone_number");
        }
        return null;
    }

}
