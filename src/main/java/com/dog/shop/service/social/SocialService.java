package com.dog.shop.service.social;

import com.dog.shop.dto.social.UserRegistrationOAuthData;
import jakarta.servlet.http.HttpServletRequest;

public interface SocialService {
    public void processKakaoOAuth(UserRegistrationOAuthData userRegistrationOAuthData);

}
