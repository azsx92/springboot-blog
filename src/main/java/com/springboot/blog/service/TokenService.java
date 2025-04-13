package com.springboot.blog.service;

import com.springboot.blog.config.jwt.TokenProvider;
import com.springboot.blog.domain.RefreshToken;
import com.springboot.blog.domain.User;
import com.springboot.blog.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
private final RefreshTokenService refreshTokenService;
private final TokenProvider tokenProvider;
private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        //토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);
        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
