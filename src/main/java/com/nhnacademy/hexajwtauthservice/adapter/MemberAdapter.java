package com.nhnacademy.hexajwtauthservice.adapter;

import com.nhnacademy.hexajwtauthservice.domain.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "hexa-shoppingmall-service", path = "/api/members")
public interface MemberAdapter {
    @GetMapping("/{memberId}")
    public Member getMemberById(@PathVariable("memberId") String memberId);
}
