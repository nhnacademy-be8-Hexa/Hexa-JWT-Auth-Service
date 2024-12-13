package com.nhnacademy.hexajwtauthservice.security.details;

import com.nhnacademy.hexajwtauthservice.domain.Member;
import com.nhnacademy.hexajwtauthservice.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PrincipalDetailsTest {

    private PrincipalDetails principalDetails;

    @BeforeEach
    void setUp() {
        Member member = new Member("user", "password", "User Name", "1234", LocalDate.of(1990, 1, 1), LocalDate.of(2020, 1, 1), LocalDateTime.now(), Role.MEMBER, null, null);

        principalDetails = new PrincipalDetails(member);
    }

    @Test
    void getAuthorities() {
        assertEquals("ROLE_MEMBER", principalDetails.getAuthorities().stream().findFirst().get().getAuthority());
    }

    @Test
    void getPassword() {
        assertEquals("password", principalDetails.getPassword());
    }

    @Test
    void getUsername() {
        assertEquals("user", principalDetails.getUsername());
    }

}