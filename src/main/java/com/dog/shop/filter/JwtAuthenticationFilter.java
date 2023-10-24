package com.dog.shop.filter;

import com.dog.shop.errorcode.ErrorCode;
import com.dog.shop.map.MapperDao;
import com.dog.shop.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MapperDao mapperDao;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/auth/signup") ||
                path.startsWith("/api/inquiry") ||
                path.startsWith("/api/keywords");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);



        if (header != null && header.startsWith("Bearer")) {
            try {
                String accessToken = header.substring(7);
                Claims claims = jwtUtil.decode(accessToken);

                boolean valid = !claims.getExpiration().before(new Date());
                if (valid) {
                    if (mapperDao.getValues(accessToken) == null) {
                        String memberId = claims.get("id", String.class);
                        Authentication authentication = jwtUtil.getAuthentication(memberId);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        request.setAttribute("exception", ErrorCode.WRONG_ID_TOKEN);
                    }
                }

            } catch (SignatureException e) {
                request.setAttribute("exception", ErrorCode.WRONG_TYPE_SIGNATURE);
            } catch (UnsupportedJwtException e) {
                request.setAttribute("exception", ErrorCode.WRONG_TOKEN);
            } catch (ExpiredJwtException e) {
                request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN);
            } catch (IllegalArgumentException e) {
                request.setAttribute("exception", ErrorCode.INVALID_TOKEN);
            }

        }

        filterChain.doFilter(request, response);

    }
}
