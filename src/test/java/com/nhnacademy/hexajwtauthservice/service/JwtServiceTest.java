package com.nhnacademy.hexajwtauthservice.service;

import com.nhnacademy.hexajwtauthservice.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Base64;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private JwtService jwtService;

    private String accessSecretKeyBase64;
    private String refreshSecretKeyBase64;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // HS256에 적합한 256비트(32바이트) 비밀키를 생성합니다.
        String accessSecretKey = "mySuperSecretAccessKeyThatIs256BitsLongAndSecure!";
        String refreshSecretKey = "mySuperSecretRefreshKeyThatIs256BitsLongAndSecure!";

        // Base64 인코딩을 해서 String 타입으로 반환하도록 합니다.
        accessSecretKeyBase64 = Base64.getEncoder().encodeToString(accessSecretKey.getBytes());
        refreshSecretKeyBase64 = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes());

        // JwtProperties에서 이 키를 사용하도록 설정합니다.
        when(jwtProperties.getAccessSecret()).thenReturn(accessSecretKeyBase64);
        when(jwtProperties.getRefreshSecret()).thenReturn(refreshSecretKeyBase64);
        when(jwtProperties.getAccessTokenExpirationTime()).thenReturn(3600);  // 예시 값 (1시간)
        when(jwtProperties.getRefreshTokenExpirationTime()).thenReturn(7200); // 예시 값 (2시간)
        when(jwtProperties.getTokenPrefix()).thenReturn("JWT"); // 예시 값
    }

    @Test
    void testGenerateAccessToken() {
        // Arrange
        String userId = "testMember";
        String role = "ADMIN";

        // 실제 JWT 생성 호출
        String accessToken = jwtService.generateAccessToken(userId, role);

        // Assert
        assertNotNull(accessToken);
        Claims claims =jwtService.parseJwt(accessToken,true);
        assertEquals(userId, claims.get("userId", String.class));
        assertEquals("ROLE_"+role, claims.get("role", String.class));
    }

    @Test
    void testGenerateRefreshToken() {
        // Arrange
        String userId = "testMember";
        String role = "ADMIN";

        // 실제 JWT 생성 호출
        String refreshToken = jwtService.generateRefreshToken(userId, role);

        // Assert
        assertNotNull(refreshToken);
        Claims claims =jwtService.parseJwt(refreshToken,false);
        assertEquals(userId, claims.get("userId", String.class));
        assertEquals("ROLE_"+role, claims.get("role", String.class));
    }

    @Test
    void testParseJwt() {
        // Arrange
        // Arrange
        String userId = "testMember";
        String role = "ADMIN";

        // 실제 JWT 생성 호출
        String refreshToken = jwtService.generateRefreshToken(userId, role);

        Claims claims = jwtService.parseJwt(refreshToken, false);

        // Assert
        assertNotNull(claims);
    }

    @Test
    void testExtractUserIdFromJwt() {
        // Arrange
        String userId = "testMember";
        String role = "ADMIN";

        // 실제 JWT 생성 호출
        String refreshToken = jwtService.generateRefreshToken(userId, role);

        // Act
        String extractUserId = jwtService.extractUserIdFromJwt(refreshToken, false);

        // Assert
        assertEquals(userId, extractUserId);
    }

    @Test
    void testExtractRoleFromJwt() {
        // Arrange
        String userId = "testMember";
        String role = "ADMIN";

        // 실제 JWT 생성 호출
        String refreshToken = jwtService.generateRefreshToken(userId, role);

        // Act
        String extractUserRole = jwtService.extractRoleFromJwt(refreshToken, false);

        // Assert
        assertEquals("ROLE_"+role, extractUserRole);
    }

    @Test
    void testExtractRestExpirationTime() {
        // Arrange
        String userId = "testMember";
        String role = "ADMIN";

        // 실제 JWT 생성 호출
        String refreshToken = jwtService.generateRefreshToken(userId, role);

        // Act
        long restExpirationTime = jwtService.extractRestExpirationTime(refreshToken);

        // Assert
        assertTrue(restExpirationTime > 0);
    }
}
