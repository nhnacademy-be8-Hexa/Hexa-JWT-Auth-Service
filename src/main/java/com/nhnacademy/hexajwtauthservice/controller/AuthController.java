package com.nhnacademy.hexajwtauthservice.controller;

import com.nhnacademy.hexajwtauthservice.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/members")
public class AuthController {

//    @GetMapping
//    public ResponseEntity<UserResponse> getMember(
//            @RequestHeader("X-USER-ID") String userId
//    ){
//
//        return ResponseEntity.ok();
//    }

}
