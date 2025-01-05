package com.nhnacademy.hexajwtauthservice.controller.token;

import com.nhnacademy.hexajwtauthservice.dto.AccessRefreshTokenResponse;
import com.nhnacademy.hexajwtauthservice.properties.JwtProperties;
import com.nhnacademy.hexajwtauthservice.service.JwtService;
import com.nhnacademy.hexajwtauthservice.service.RefreshTokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public TokenController(JwtProperties jwtProperties, RefreshTokenService refreshTokenService) {
        this.jwtProperties = jwtProperties;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = new JwtService(jwtProperties);
    }

    // 접근 토큰 및 리프레시 토큰 재 발급
    @PostMapping("/api/auth/reissue")
    public AccessRefreshTokenResponse reissueAccessRefreshToken(@RequestHeader("Authorization") String refreshToken){

        refreshToken = refreshToken.replace("Bearer ","").trim();

        String userId = jwtService.extractUserIdFromJwt(refreshToken,false);

        String userRole = jwtService.extractRoleFromJwt(refreshToken,false);

        userRole = userRole.replace("ROLE_","");

        String accessTokenOutput = jwtService.generateAccessToken(userId,userRole);

        String refreshTokenOutput = jwtService.generateRefreshToken(userId,userRole);

        refreshTokenService.deleteRefreshToken(userId);

        refreshTokenService.saveRefreshToken(userId,refreshTokenOutput);

        return new AccessRefreshTokenResponse(accessTokenOutput,refreshTokenOutput);
    }

}
