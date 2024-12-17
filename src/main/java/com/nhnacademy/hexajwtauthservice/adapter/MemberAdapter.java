package com.nhnacademy.hexajwtauthservice.adapter;

import com.nhnacademy.hexajwtauthservice.domain.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hexa-shoppingmall-service")
public interface MemberAdapter {
    @GetMapping("/api/members/{memberId}")
    public Member getMember(@PathVariable String memberId);
}
