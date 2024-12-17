package com.nhnacademy.hexajwtauthservice.security.details;

import com.nhnacademy.hexajwtauthservice.adapter.MemberAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    private MemberAdapter memberAdapter;

    public CustomUserDetailsService(MemberAdapter memberAdapter) {
        this.memberAdapter = memberAdapter;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // db에서 유저 가져오기 (페인 클라 어댑터)
        return new PrincipalDetails(memberAdapter.getMember(username));

    }
}
