package com.nhnacademy.hexajwtauthservice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.hexajwtauthservice.domain.Member;
import com.nhnacademy.hexajwtauthservice.domain.Role;
import com.nhnacademy.hexajwtauthservice.dto.LoginRequest;
import com.nhnacademy.hexajwtauthservice.properties.JwtProperties;
import com.nhnacademy.hexajwtauthservice.security.details.PrincipalDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private AuthenticationManager authenticationManager;

    private JwtProperties jwtProperties;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setLoginUrl("/login");
        jwtProperties.setTokenPrefix("Bearer");
        jwtProperties.setExpirationTime(3600);

        // Secure key generation for HS256
        SecretKey secureKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        jwtProperties.setSecret(Base64.getEncoder().encodeToString(secureKey.getEncoded()));

        objectMapper = new ObjectMapper();
        authenticationManager = mock(AuthenticationManager.class);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtProperties, objectMapper);
    }

    @Test
    void attemptAuthentication_validCredentials_shouldAuthenticate() throws Exception {
        LoginRequest loginRequest = new LoginRequest("user", "password");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setContent(objectMapper.writeValueAsBytes(loginRequest));

        MockHttpServletResponse response = new MockHttpServletResponse();

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        Authentication result = jwtAuthenticationFilter.attemptAuthentication(request, response);

        assertNotNull(result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void successfulAuthentication_shouldGenerateJwtToken() throws Exception {
        Member member = new Member("user", "password", "User Name", "1234", LocalDate.of(1990, 1, 1), LocalDate.of(2020, 1, 1), LocalDateTime.now(), Role.MEMBER, null, null);
        PrincipalDetails principalDetails = new PrincipalDetails(member);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principalDetails);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthenticationFilter.successfulAuthentication(request, response, null, authentication);

        String jwtToken = response.getContentAsString();

        assertNotNull(jwtToken);
        assertTrue(Jwts.parser().setSigningKey(Base64.getDecoder().decode(jwtProperties.getSecret())).parseClaimsJws(jwtToken).getBody().getExpiration() != null);
    }


    @Test
    void attemptAuthentication_invalidCredentials_shouldReturnNull() {
        LoginRequest loginRequest = new LoginRequest("invalid_user", "invalid_password");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            request.setContent(objectMapper.writeValueAsBytes(loginRequest));
        } catch (Exception e) {
            fail("Failed to serialize login request");
        }

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new NullPointerException("Invalid credentials"));

        Authentication result = jwtAuthenticationFilter.attemptAuthentication(request, response);

        assertNull(result);
    }

    @Test
    void successfulAuthentication_nullAuthResult_shouldReturnNullResponse() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthenticationFilter.successfulAuthentication(request, response, null, null);

        String responseContent = response.getContentAsString();
        assertEquals("null", responseContent);
    }

    @Test
    void objectMappingError(){
        // Mock 객체 생성
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

        // request.getInputStream()이 IOException을 던지도록 설정
        try {
            when(request.getInputStream()).thenThrow(IOException.class);
        } catch (IOException e) {
            // 예외가 발생하지 않도록 예외 처리
        }

        // 예외가 발생하는지 검증
        assertThrows(RuntimeException.class, () -> {
            jwtAuthenticationFilter.attemptAuthentication(request, response);
        });
    }

}

