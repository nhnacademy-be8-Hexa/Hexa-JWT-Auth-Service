package com.nhnacademy.hexajwtauthservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String id;
    private String password;
}
