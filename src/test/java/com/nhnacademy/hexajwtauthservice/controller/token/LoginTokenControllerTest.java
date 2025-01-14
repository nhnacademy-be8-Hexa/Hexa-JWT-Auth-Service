package com.nhnacademy.hexajwtauthservice.controller.token;

import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.hexajwtauthservice.adapter.MemberAdapter;
import com.nhnacademy.hexajwtauthservice.domain.Member;
import com.nhnacademy.hexajwtauthservice.domain.Role;
import com.nhnacademy.hexajwtauthservice.dto.AccessRefreshTokenResponse;
import com.nhnacademy.hexajwtauthservice.dto.LoginRequest;
import com.nhnacademy.hexajwtauthservice.service.JwtService;
import com.nhnacademy.hexajwtauthservice.service.BlackListRefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import jakarta.ws.rs.BadRequestException;

class LoginTokenControllerTest {

    @Mock
    private MemberAdapter memberAdapter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginTokenController loginTokenController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private LoginRequest loginRequest;



    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        member = new Member();
        member.setMemberId("memberId");
        member.setMemberPassword("encodedPassword");
        Role role = Role.MEMBER;
        member.setMemberRole(role);


    }

    @Test
    void testGenerateAccessRefreshToken_validCredentials() {
        // Given
        when(loginRequest.getId()).thenReturn(member.getMemberId());
        when(loginRequest.getPassword()).thenReturn(member.getMemberPassword());
        when(memberAdapter.getMember(anyString())).thenReturn(member);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateAccessToken(anyString(), anyString())).thenReturn("validAccessToken");
        when(jwtService.generateRefreshToken(anyString(), anyString())).thenReturn("validRefreshToken");

        // When
        AccessRefreshTokenResponse response = loginTokenController.generateAccessRefreshToken(request, this.response, loginRequest);

        // Then
        assertNotNull(response);
        assertEquals("validAccessToken", response.getAccessToken());
        assertEquals("validRefreshToken", response.getRefreshToken());
    }

    @Test
    void testGenerateAccessRefreshToken_invalidMemberId() {
        // Given
        when(memberAdapter.getMember(anyString())).thenReturn(null);

        // When
        AccessRefreshTokenResponse response = loginTokenController.generateAccessRefreshToken(request, this.response, loginRequest);

        // Then
        assertNull(response);
    }

    @Test
    void testGenerateAccessRefreshToken_passwordMismatch() {
        // Given
        when(memberAdapter.getMember(anyString())).thenReturn(member);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // When
        AccessRefreshTokenResponse response = loginTokenController.generateAccessRefreshToken(request, this.response, loginRequest);

        // Then
        assertNull(response);
    }




    @Test
    void testGenerateAccessRefreshToken_invalidPasswordFormat() {
        // Given
        member.setMemberPassword(null);  // Invalid password

        // When
        AccessRefreshTokenResponse response = loginTokenController.generateAccessRefreshToken(request, this.response, loginRequest);

        // Then
        assertNull(response);
    }
}
