package com.nhnacademy.hexajwtauthservice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.hexajwtauthservice.dto.LoginRequest;
import com.nhnacademy.hexajwtauthservice.properties.JwtProperties;
import com.nhnacademy.hexajwtauthservice.security.details.PrincipalDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter  {
    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties, ObjectMapper objectMapper) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtProperties = jwtProperties;
        this.objectMapper = objectMapper;

        setFilterProcessesUrl(jwtProperties.getLoginUrl());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequest loginRequest = null;
        try {
            loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            // 인증 요청 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getId(), loginRequest.getPassword());

            // 인증 처리
            return authenticationManager.authenticate(authenticationToken);

        } catch (AuthenticationException | NullPointerException e) {
            // 인증 실패 시 null 응답 처리
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        if(authResult == null) {
            // 인증 실패로 프론트에 null 전달
            PrintWriter writer = response.getWriter();
            writer.write("null"); // 프론트에 null 응답
            writer.close();
            return;
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, jwtProperties.getExpirationTime() );

        String role = principalDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority) // 권한 이름만 추출
                .findFirst() // 첫 번째 요소 가져오기
                .orElse(null); // 없으면 null

        String jwtToken = Jwts.builder()
                .setHeaderParam("typ", jwtProperties.getTokenPrefix())
                .claim("userId", principalDetails.getUsername())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();

        PrintWriter printWriter = response.getWriter();
        printWriter.write(jwtToken);
        printWriter.close();
    }
}
