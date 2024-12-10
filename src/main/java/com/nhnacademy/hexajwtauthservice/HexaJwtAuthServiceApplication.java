package com.nhnacademy.hexajwtauthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(value = {com.nhnacademy.hexajwtauthservice.properties.JwtProperties.class})
public class HexaJwtAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HexaJwtAuthServiceApplication.class, args);
    }

}
