package com.nhnacademy.hexajwtauthservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class AccessRefreshTokenResponse {

    private String accessToken;
    private String refreshToken;
}
