//package com.dog.shop.config;
//
//import com.dog.shop.custom.CustomUserDetailService;
//import com.dog.shop.filter.JwtAuthenticationFilter;
//import com.dog.shop.filter.JwtAuthenticationSuccessHandler;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
//
//import java.util.Arrays;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@EnableWebSecurity
//@RequiredArgsConstructor
//@Configuration
//public class NewSecuityConfig  {
//
//    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//    private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//        //.requestMatchers("/resources/static/**");
//    }
//
////    @Bean
////    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
////        return http.csrf(csrf -> csrf.disable())
////                .formLogin(login -> login
////                        .loginPage("/login")
////                        .loginProcessingUrl("/login-process")
////                        .usernameParameter("email")
////                        .passwordParameter("password")
////                        .defaultSuccessUrl("/index", true)
////                        .successHandler(jwtAuthenticationSuccessHandler)
////                        .permitAll()
////                )
////                .httpBasic(basic -> basic.disable())
////                .authorizeHttpRequests( auth -> {
////                    auth.requestMatchers("/api/users/welcome", "/api/userinfos/new",
////                                    "/api/userinfos/login", "/index", "/favicon.ico").permitAll()
////                            .requestMatchers("/api/users/**").authenticated();
////                })
////                //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .httpBasic(withDefaults())
////                .authenticationProvider(authenticationProvider())
////                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
////                .build();
////    }
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider
//                = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService());
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new CustomUserDetailService();
//    }
//
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable())
//                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
//                .sessionManagement(
//                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling(
//                        exceptionHandling -> exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint))
//                .authorizeHttpRequests(requests -> requests
//                        .requestMatchers("/index").permitAll()
//                        .requestMatchers("/user/**").authenticated()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/server/logout")
//                        .logoutSuccessUrl("/")
//                        .deleteCookies("refreshToken", "JSESSIONID")  // Here, you can add multiple cookie names
//                        .clearAuthentication(true)
//                );
//        return http.build();
//    }
//
////    @Bean
////    @Order(1)
////    public SecurityFilterChain formLoginFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
////            throws Exception {
////        return http.csrf(csrf -> csrf.disable())
////                .authorizeHttpRequests(auth -> {
////                    //auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
////                    auth.requestMatchers("/login", "/index").permitAll()
//////                            .requestMatchers("/api/users/**").authenticated()
////                            .requestMatchers("/users/**").authenticated();
////                    //.requestMatchers("/api/users/**").authenticated();
////                    //.requestMatchers("/**").denyAll();
////                })
////                //.formLogin(withDefaults())
////                .formLogin(login -> login
////                        .loginPage("/login")
////                        .loginProcessingUrl("/login-process")
////                        .usernameParameter("email")
////                        .passwordParameter("password")
////                        .defaultSuccessUrl("/index", true)
////                        .permitAll()
////                )
////                .logout((logout) -> logout.logoutUrl("/app-logout")
////                        .deleteCookies("JSESSIONID")
////                        .logoutSuccessUrl("/")
////                )
////                .build();
////    }
//
//  /*  .authorizeRequests(authorize -> authorize
//            .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/api/user/**").hasRole("USER")
//                        .anyRequest().authenticated()
//                )*/
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        // config.setAllowedOrigins(Arrays.asList("http://210.180.118.201:8500",
//        // "http://localhost:3000"));
//        config.setAllowedOrigins(Arrays.asList("*")); // 모든 도메인 허용
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//}