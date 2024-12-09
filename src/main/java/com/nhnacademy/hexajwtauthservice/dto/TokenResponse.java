package com.nhnacademy.hexajwtauthservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TokenResponse {
    private String accessToken;
    private String tokenType;
    private Integer expiresIn;
}
