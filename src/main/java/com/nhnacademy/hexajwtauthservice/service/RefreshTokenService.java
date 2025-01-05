package com.nhnacademy.hexajwtauthservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

    private static final long EXPIRATION_TIME = 10L;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 토큰 저장 및 갱신
    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set(userId, refreshToken, EXPIRATION_TIME, TimeUnit.MINUTES);
    }

    // 토큰 조회
    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get(userId);
    }

    // 토큰 삭제
    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(userId);
    }
}