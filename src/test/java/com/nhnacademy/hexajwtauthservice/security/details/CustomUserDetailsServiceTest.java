package com.nhnacademy.hexajwtauthservice.security.details;

import com.nhnacademy.hexajwtauthservice.adapter.MemberAdapter;
import com.nhnacademy.hexajwtauthservice.domain.Member;
import com.nhnacademy.hexajwtauthservice.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private MemberAdapter memberAdapter;
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        memberAdapter = mock(MemberAdapter.class); // MemberAdapter Mock 생성
        customUserDetailsService = new CustomUserDetailsService(memberAdapter); // Service 생성
    }

    @Test
    void loadUserByUsername_WithValidUsername_ShouldReturnPrincipalDetails() {
        // given: Mocked Member
        Member mockMember = new Member();
        mockMember.setMemberId("testUser");
        mockMember.setMemberPassword("password123");
        mockMember.setMemberRole(Role.MEMBER);

        when(memberAdapter.getMember("testUser")).thenReturn(mockMember);

        // when: loadUserByUsername 호출
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testUser");

        // then: 반환된 UserDetails 검증
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof PrincipalDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_MEMBER")));

        // MemberAdapter 호출 확인
        verify(memberAdapter, times(1)).getMember("testUser");
    }

    @Test
    void loadUserByUsername_WithInvalidUsername_ShouldThrowUsernameNotFoundException() {
        // given: Mock 설정 - 사용자를 찾지 못하는 경우
        when(memberAdapter.getMember("invalidUser")).thenThrow(new UsernameNotFoundException("User not found"));

        // when & then: 예외 확인
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("invalidUser");
        });

        // MemberAdapter 호출 확인
        verify(memberAdapter, times(1)).getMember("invalidUser");
    }
}
