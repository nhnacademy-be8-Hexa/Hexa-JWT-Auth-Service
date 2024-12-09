package com.nhnacademy.hexajwtauthservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private final String userId;
    private final String username;
    private final String password;

}
