package com.nhnacademy.hexajwtauthservice.controller.token;

import com.nhnacademy.hexajwtauthservice.service.BlackListRefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/refreshTokenBlacklists")
public class RedisBlackListRefreshTokenController {

    private final BlackListRefreshTokenService refreshTokenService;

    public RedisBlackListRefreshTokenController(BlackListRefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    // Refresh token 저장 및 갱신
    @PostMapping
    public ResponseEntity<Void> addToBlackListToken(@RequestHeader("Authorization") String refreshToken) {

        refreshToken = refreshToken.replace("Bearer ","").trim();

        refreshTokenService.addToBlackList(refreshToken);

        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    // 해당 userId에 refresh token 가져오기
    @GetMapping
    public Boolean isTokenBlackListed(@RequestHeader("Authorization") String refreshToken) {

        refreshToken = refreshToken.replace("Bearer ","").trim();

        return refreshTokenService.isTokenBlackListed(refreshToken);
    }

    // Refresh token 삭제
    @DeleteMapping
    public ResponseEntity<Void> removeFromBlackList(@RequestHeader("Authorization") String refreshToken) {

        refreshToken = refreshToken.replace("Bearer ","").trim();

        refreshTokenService.removeFromBlackList(refreshToken);

        return ResponseEntity.noContent().build();
    }
}
