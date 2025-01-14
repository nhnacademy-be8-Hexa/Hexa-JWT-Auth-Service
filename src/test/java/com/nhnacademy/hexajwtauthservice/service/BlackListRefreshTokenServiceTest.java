package com.nhnacademy.hexajwtauthservice.service;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BlackListRefreshTokenServiceTest {

    @Mock
    private JwtService jwtService;  // JwtService를 Mock 객체로 생성

    @Mock
    private RedisTemplate<String, String> redisTemplate;  // RedisTemplate을 Mock 객체로 생성

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private BlackListRefreshTokenService blackListRefreshTokenService;  // 테스트하려는 서비스 객체

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mock 객체 초기화
    }


    @Test
    void isTokenBlackListed_ShouldReturnTrue_WhenTokenIsBlacklisted() {
        // Arrange
        String refreshToken = "test-refresh-token";
        when(redisTemplate.hasKey("blacklist:" + refreshToken)).thenReturn(true);  // Redis에서 해당 키가 있는지 확인

        // Act
        boolean result = blackListRefreshTokenService.isTokenBlackListed(refreshToken);

        // Assert
        assertTrue(result);  // 토큰이 블랙리스트에 있으면 true 반환
    }

    @Test
    void isTokenBlackListed_ShouldReturnFalse_WhenTokenIsNotBlacklisted() {
        // Arrange
        String refreshToken = "test-refresh-token";
        when(redisTemplate.hasKey("blacklist:" + refreshToken)).thenReturn(false);  // Redis에서 해당 키가 없는지 확인

        // Act
        boolean result = blackListRefreshTokenService.isTokenBlackListed(refreshToken);

        // Assert
        assertFalse(result);  // 토큰이 블랙리스트에 없으면 false 반환
    }

    @Test
    void removeFromBlackList_ShouldRemoveTokenFromRedis() {
        // Arrange
        String refreshToken = "test-refresh-token";

        // Act
        blackListRefreshTokenService.removeFromBlackList(refreshToken);

        // Assert
        verify(redisTemplate).delete("blacklist:" + refreshToken);  // Redis에서 해당 키 삭제 여부 확인
    }
}
