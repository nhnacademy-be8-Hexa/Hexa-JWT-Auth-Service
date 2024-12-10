package com.nhnacademy.hexajwtauthservice.controller;

import com.nhnacademy.hexajwtauthservice.adapter.MemberAdapter;
import com.nhnacademy.hexajwtauthservice.domain.Member;
import com.nhnacademy.hexajwtauthservice.domain.MemberStatus;
import com.nhnacademy.hexajwtauthservice.domain.Rating;
import com.nhnacademy.hexajwtauthservice.domain.Role;
import com.nhnacademy.hexajwtauthservice.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/members")
public class AuthController {

    private final MemberAdapter memberAdapter;

    @GetMapping
    public ResponseEntity<UserResponse> getMember(
            @RequestHeader("X-USER-ID") String userId,
            Principal principal
    ){
//        Member member = memberAdapter.getMemberById(userId);
//        UserResponse response = new UserResponse(
//                member.getMemberId(),
//                member.getMemberPassword(),
//                member.getMemberName(),
//                member.getMemberNumber(),
//                member.getMemberBirthAt(),
//                member.getMemberCreatedAt(),
//                member.getMemberLastLoginAt(),
//                member.getMemberRole().toString(),
//                member.getRating().getRatingId().toString(),
//                member.getMemberStatus().getStatusId().toString()
//        );


        return ResponseEntity.ok(
                new UserResponse("testMember",
                        new BCryptPasswordEncoder().encode("1234"),
                        "test name",
                        "01012345678",
                        LocalDate.now(),
                        LocalDate.now(),
                        LocalDateTime.now(),
                        Role.MEMBER.toString(),
                        "1",
                        "1"
                )
        );
    }

}
