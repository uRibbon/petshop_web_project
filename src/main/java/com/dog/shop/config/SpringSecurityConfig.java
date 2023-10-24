package com.dog.shop.config;

import com.dog.shop.filter.JwtAuthenticationFilter;
import com.dog.shop.utils.JwtUtil;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtUtil jwtUtil;

    @Autowired
    public SpringSecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                                JwtAuthenticationFilter jwtAuthenticationFilter,
                                JwtUtil jwtUtil) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtUtil = jwtUtil;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable()
                .authorizeHttpRequests(request -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .anyRequest().authenticated()    // 어떠한 요청이라도 인증필요
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint))
                .formLogin(login -> login
                        .loginPage("/login")    // [A] 커스텀 로그인 페이지 지정
                        .loginProcessingUrl("/login-process")    // [B] submit 받을 url
                        .usernameParameter("email")    // [C] submit할 아이디
                        .passwordParameter("password")    // [D] submit할 비밀번호
                        .defaultSuccessUrl("/index", true)
                        .successHandler(new JwtAuthenticationSuccessHandler(jwtUtil))  // 로그인 성공 핸들러 설정
                        .permitAll()
                )
                .logout(Customizer.withDefaults());    // 로그아웃은 기본설정으로 (/logout으로 인증해제)

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}