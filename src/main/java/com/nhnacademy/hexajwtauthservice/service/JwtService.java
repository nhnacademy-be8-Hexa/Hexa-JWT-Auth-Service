package com.nhnacademy.hexajwtauthservice.service;

import com.nhnacademy.hexajwtauthservice.domain.Member;
import com.nhnacademy.hexajwtauthservice.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }


    /**
     * Access Token을 생성하는 메서드
     */

    public String generateAccessToken(String memberId , String role) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, jwtProperties.getAccessTokenExpirationTime());

        return Jwts.builder()
                .setHeaderParam("typ", jwtProperties.getTokenPrefix())
                .claim("userId", memberId)
                .claim("role", "ROLE_" + role)
                .setIssuedAt(new Date())
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getAccessSecret())
                .compact();
    }

    /**
     * Refresh Token을 생성하는 메서드
     */
    public  String generateRefreshToken(String memberId , String role) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, jwtProperties.getRefreshTokenExpirationTime());

        return Jwts.builder()
                .setHeaderParam("typ", jwtProperties.getTokenPrefix())
                .claim("userId", memberId)
                .claim("role", "ROLE_" + role)
                .claim("uuid", UUID.randomUUID().toString())
                .claim("currentTimeMillis", System.currentTimeMillis())
                .setIssuedAt(new Date())
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getRefreshSecret())
                .compact();
    }

    /**
     * JWT에서 클레임을 파싱하는 메서드
     */
    public Claims parseJwt(String jwt, boolean isAccessToken) {
        String secretKey = isAccessToken ? jwtProperties.getAccessSecret() : jwtProperties.getRefreshSecret();

            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

    }

    /**
     * JWT에서 userId를 추출하는 메서드
     */
    public String extractUserIdFromJwt(String jwt, boolean isAccessToken) {
        Claims claims = parseJwt(jwt, isAccessToken);
        return claims.get("userId", String.class);
    }

    /**
     * JWT에서 userRole을 추출하는 메서드
     */
    public String extractRoleFromJwt(String jwt, boolean isAccessToken) {
        Claims claims = parseJwt(jwt, isAccessToken);
        return claims.get("role", String.class);
    }

    /**
     * 만료시간에서 현재시간 차감한 시간 반환
     */
    public long extractRestExpirationTime(String refreshToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getRefreshSecret())  // JWT 서명에 사용된 비밀키를 넣어야 합니다.
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

        // 토큰의 exp 필드에서 만료 시간을 추출 (단위: milliseconds)
        Date expiration = claims.getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

}
