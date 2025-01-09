package com.nhnacademy.hexajwtauthservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BlackListRefreshTokenService {

    private final JwtService jwtService;
    private final RedisTemplate<String, String> redisTemplate;

    public BlackListRefreshTokenService(JwtService jwtService, RedisTemplate<String, String> redisTemplate) {
        this.jwtService = jwtService;
        this.redisTemplate = redisTemplate;
    }

    private static final String BLACKLIST_PREFIX = "blacklist:";  // 블랙리스트 키 prefix

    // 블랙리스트에 토큰 등록
    public void addToBlackList(String refreshToken) {
        long expirationTime =  jwtService.extractRestExpirationTime(refreshToken);
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + refreshToken, refreshToken, expirationTime, TimeUnit.MILLISECONDS);
    }

    // 토큰이 블랙리스트에 있는지 확인
    public boolean isTokenBlackListed(String refreshToken) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + refreshToken);
    }

    // 블랙리스트에서 토큰 삭제
    public void removeFromBlackList(String refreshToken) {
        redisTemplate.delete(BLACKLIST_PREFIX + refreshToken);
    }
}