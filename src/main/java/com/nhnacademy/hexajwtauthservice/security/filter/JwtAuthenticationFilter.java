package com.nhnacademy.hexajwtauthservice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.hexajwtauthservice.dto.LoginRequest;
import com.nhnacademy.hexajwtauthservice.dto.TokenResponse;
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

        log.debug("JwtAuthenticationFilter : {}", loginRequest);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getId(),loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, jwtProperties.getExpirationTime() );

        String jwtToken = Jwts.builder()
                .claim("userId", principalDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();

        TokenResponse tokenResponse = new TokenResponse(jwtToken, jwtProperties.getTokenPrefix(), jwtProperties.getExpirationTime());
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tokenResponse);

        PrintWriter printWriter = response.getWriter();
        printWriter.write(result);
        printWriter.close();

    }
}
