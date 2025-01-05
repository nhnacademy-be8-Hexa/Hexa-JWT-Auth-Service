package com.nhnacademy.hexajwtauthservice.controller.token;

import com.nhnacademy.hexajwtauthservice.properties.JwtProperties;
import com.nhnacademy.hexajwtauthservice.service.JwtService;
import com.nhnacademy.hexajwtauthservice.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/refreshTokenRedis")
public class RedisRefreshTokenController {

    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public RedisRefreshTokenController(JwtProperties jwtProperties, RefreshTokenService refreshTokenService) {
        this.jwtProperties = jwtProperties;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = new JwtService(jwtProperties);
    }

    // Refresh token 저장 및 갱신
    @PostMapping
    public ResponseEntity<Void> saveToken(@RequestHeader("Authorization") String refreshToken) {

        refreshToken = refreshToken.replace("Bearer ","").trim();

        String userId  = jwtService.extractUserIdFromJwt(refreshToken,false);
        refreshTokenService.saveRefreshToken(userId,refreshToken);

        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    // 해당 userId에 refresh token 가져오기
    @GetMapping
    public String getRefreshToken(@RequestHeader("Authorization") String refreshToken) {

        refreshToken = refreshToken.replace("Bearer ","").trim();

        String userId  = jwtService.extractUserIdFromJwt(refreshToken,false);
        return refreshTokenService.getRefreshToken(userId);
    }

    // Refresh token 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteToken(@RequestHeader("Authorization") String refreshToken) {

        refreshToken = refreshToken.replace("Bearer ","").trim();

        String userId  = jwtService.extractUserIdFromJwt(refreshToken,false);

        refreshTokenService.deleteRefreshToken(userId);

        return ResponseEntity.noContent().build();
    }
}
