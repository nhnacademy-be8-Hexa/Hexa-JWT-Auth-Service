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
    private String secret;
    private Integer expirationTime;
    private String tokenPrefix;
    private String headerString;

    private String loginUrl;
}
