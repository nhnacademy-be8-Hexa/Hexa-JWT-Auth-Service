package com.nhnacademy.hexajwtauthservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String accessSecret;
    private String refreshSecret;
    private Integer accessTokenExpirationTime;
    private Integer refreshTokenExpirationTime;
    private String tokenPrefix;
    private String headerString;
}
