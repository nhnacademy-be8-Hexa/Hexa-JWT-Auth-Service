package com.nhnacademy.hexajwtauthservice.security.details;

import com.nhnacademy.hexajwtauthservice.adapter.MemberAdapter;
import com.nhnacademy.hexajwtauthservice.domain.Member;
import com.nhnacademy.hexajwtauthservice.domain.MemberStatus;
import com.nhnacademy.hexajwtauthservice.domain.Rating;
import com.nhnacademy.hexajwtauthservice.domain.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CustomUserDetailsService implements UserDetailsService {

    private MemberAdapter memberAdapter;

    public CustomUserDetailsService(MemberAdapter memberAdapter) {
        this.memberAdapter = memberAdapter;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // db에서 유저 가져오기 (페인 클라 어댑터)
//        return new PrincipalDetails(memberAdapter.getMemberById(username));

        // 테스트용 급조 db
        Map<String, Object> map = new HashMap<>();
        map.put("testMember", new Member("testMember",
                new BCryptPasswordEncoder().encode("1234"),
                "test name",
                "01012345678",
                LocalDate.now(),
                LocalDate.now(),
                LocalDateTime.now(),
                Role.MEMBER,
                new Rating(1L, "gold", 20),
                new MemberStatus(1L, "online")
        ));

        map.put("testMember2", new Member("testMember2",
                new BCryptPasswordEncoder().encode("12345"),
                "test name",
                "01012345678",
                LocalDate.now(),
                LocalDate.now(),
                LocalDateTime.now(),
                Role.MEMBER,
                new Rating(1L, "gold", 20),
                new MemberStatus(1L, "online")
        ));

        return new PrincipalDetails(
                (Member) map.get(username)
        );
    }
}
