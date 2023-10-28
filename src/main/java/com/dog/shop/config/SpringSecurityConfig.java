package com.dog.shop.config;

import com.dog.shop.filter.JwtAuthenticationFilter;
import com.dog.shop.myenum.Role;
import com.dog.shop.utils.JwtUtil;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.management.relation.RoleStatus;

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
                .headers((headerCongfig) ->
                        headerCongfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
                .authorizeHttpRequests(request -> request
//                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
//                        .requestMatchers("/auth/signup").permitAll()
//                        .anyRequest().authenticated()    // 어떠한 요청이라도 인증필요
                                .requestMatchers("/api/register/**").authenticated()
//                                .requestMatchers("/api").hasRole(Role.ADMIN.getKey())
                                .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig
                                .authenticationEntryPoint(unauthorizedEntryPoint)
                 //               .accessDeniedHandler(accessDeniedHandler)
                )
//                .exceptionHandling(
//                        exceptionHandling -> exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint))
                .formLogin(login -> login
                        .loginPage("/login")    // [A] 커스텀 로그인 페이지 지정
                        .loginProcessingUrl("/login-process")    // [B] submit 받을 url
                        .usernameParameter("email")    // [C] submit할 아이디
                        .passwordParameter("password")    // [D] submit할 비밀번호
                        .defaultSuccessUrl("/", true)
                        .successHandler(new JwtAuthenticationSuccessHandler(jwtUtil))  // 로그인 성공 핸들러 설정
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                        //.invalidateHttpSession(true) // HTTP 세션을 무효화
                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키를 삭제
                );

        return http.build();
    }


    private final AuthenticationEntryPoint unauthorizedEntryPoint =
            (request, response, authException) -> {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                request.getRequestDispatcher("/unauthorized").forward(request, response);
            };

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}