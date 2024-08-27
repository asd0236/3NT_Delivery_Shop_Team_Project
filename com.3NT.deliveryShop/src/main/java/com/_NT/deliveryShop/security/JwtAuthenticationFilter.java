package com._NT.deliveryShop.security;

import com._NT.deliveryShop.domain.dto.UserDto;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.domain.entity.UserRoleEnum;
import com._NT.deliveryShop.repository.UserRepository;
import com._NT.deliveryShop.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        setFilterProcessesUrl("/api/v1/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserDto.Login requestDto = new ObjectMapper().readValue(request.getInputStream(), UserDto.Login.class);

            User user = userRepository.findByUsername(requestDto.getUsername()).orElse(null);
            if (user == null) {
                throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
            }
            Long userId = user.getUserId();

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String userId = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(userId, role);

        // JSON 형태로 응답 작성
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JWT 토큰을 JSON으로 감싸서 응답에 작성
        String jsonResponse = String.format("{\"token\": \"%s\"}", token.substring(7));
        response.getWriter().write(jsonResponse);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }

}