package com.dog.shop.help;

import com.dog.shop.domain.User;
import com.dog.shop.repository.UserRepository;
import com.dog.shop.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtHelper {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT-TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public Optional<User> extractUserFromToken(String token) {
        if (token != null) {
            String email = jwtUtil.getEmailFromToken(token);
            if (email != null) {
                return userRepository.findByEmail(email);
            }
        }
        return Optional.empty();
    }
}
