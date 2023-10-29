package com.dog.shop.utils;

import com.dog.shop.custom.CustomUserDetailService;
import com.dog.shop.errorcode.ErrorCode;
import com.dog.shop.exception.CommonException;
import com.dog.shop.myenum.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.token-validity}")
    private long TOKEN_VALIDITY;  // JWT 토큰의 유효 시간

    /**
     * JWT 토큰의 유효 시간을 밀리초 단위로 반환합니다.
     *
     * @return 토큰의 유효 시간 (밀리초)
     */
    public long getTokenValidityInMilliseconds() {
        return TOKEN_VALIDITY;
    }

    // access 토큰 유효 시간 3m
    // private final long accessTokenValidTime = Duration.ofMinutes(30).toMillis();
    // 테스트시 30일
    private final long accessTokenValidTime = Duration.ofDays(30).toMillis();

    // 리프레시 토큰 유효시간 | 2주
    private final long refreshTokenValidTime = Duration.ofDays(7).toMillis();

    @Autowired
    private CustomUserDetailService customUserDetailService;

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

                //.setId(id)
    public String createAccessToken(String email, Role role) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("ADMIN") // JWT 발급자를 설정
                .setIssuedAt(now) // JWT 발급 시간을 설정
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
//                .claim("id", Long.toString(memberId)) // JWT에 추가할 클레임 정보를 설정
                //.claim("id", id) // JWT에 추가할 클레임 정보를 설정
                .claim("email", email) // JWT에 추가할 클레임 정보를 설정
                .claim("role", role)
                .signWith(key) // JWT 서명 알고리즘과 서명 키를 설정
                .compact(); // JWT 문자열을 반환
    }

    /*public String createRefreshToken(Long memberId, Role role) {
        Date now = new Date();
        return Jwts.builder()
                .setId(Long.toString(memberId))
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("ADMIN")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .claim("id", Long.toString(memberId))
                .claim("role", role)
                .signWith(key)
                .compact();
    }*/

    public Long getExpirations(String token) {
        // accessToken 남은 유효시간
        Date expiration;
        try {
            expiration = decode(token).getExpiration();
        } catch (Exception e) {
            throw new CommonException(ErrorCode.INVALID_TOKEN, HttpStatus.BAD_REQUEST);
        }
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
    //@Cacheable(value = "userAuthentication", key = "#id") // TODO 사용자 정보 변경 시 캐시 무효화 필수
    public Authentication getAuthentication(String id) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(id);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }



    // Claims 객체는 JWT에 저장된 정보를 담고 있는 객체
    public Claims decode(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = decode(token); // 이미 만들어진 decode 함수를 사용
            return claims.get("email", String.class);
        } catch (Exception e) {
            // 토큰이 유효하지 않은 경우나 이메일 정보가 없는 경우 예외 처리
            throw new CommonException(ErrorCode.INVALID_TOKEN, HttpStatus.BAD_REQUEST);
        }
    }


    public static Long getMemberId() {

        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDetails userDetails = (UserDetails) principal;
            return Long.parseLong(userDetails.getUsername());
        } catch (Exception e) {
            return 0L;
        }
    }


}
