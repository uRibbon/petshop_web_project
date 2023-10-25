package com.dog.shop.dto.social;

import com.dog.shop.myenum.LoginType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationOAuthData {
	private String accessToken;
	private LoginType type;  // 여기에 Enum을 추가합니다.
}