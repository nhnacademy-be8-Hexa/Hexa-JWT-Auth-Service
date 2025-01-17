package com.nhnacademy.hexajwtauthservice.controller.token;

import com.nhnacademy.hexajwtauthservice.adapter.MemberAdapter;
import com.nhnacademy.hexajwtauthservice.domain.Member;
import com.nhnacademy.hexajwtauthservice.dto.AccessRefreshTokenResponse;
import com.nhnacademy.hexajwtauthservice.dto.LoginRequest;
import com.nhnacademy.hexajwtauthservice.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginTokenController {

    private final MemberAdapter memberAdapter;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginTokenController(MemberAdapter memberAdapter, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.memberAdapter = memberAdapter;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    // 로그인을 하면 기존 리프레시 토큰은 무조건 갱신하게 됨
    @PostMapping("/api/auth/login")
    public AccessRefreshTokenResponse generateAccessRefreshToken(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginRequest loginrequest){


        Member member;

        try {
                member = memberAdapter.getMember(loginrequest.getId());
        } catch (BadRequestException e) {
            return null;
        } catch (Exception e) {
            return null;
        }

        if(member == null || member.getMemberId()==null){
            return null;
        }

        if (passwordEncoder.matches(loginrequest.getPassword(), member.getMemberPassword())) {

            String jwtAccessToken = jwtService.generateAccessToken(member.getMemberId(),member.getMemberRole().toString());
            String jwtRefreshToken = jwtService.generateRefreshToken(member.getMemberId(),member.getMemberRole().toString());


            return new AccessRefreshTokenResponse(jwtAccessToken , jwtRefreshToken );
        }

        else {

            return null;
        }

    }
}
