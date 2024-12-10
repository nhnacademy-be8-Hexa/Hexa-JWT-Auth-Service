package com.nhnacademy.hexajwtauthservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberStatus {
    private Long statusId;
    private String statusName;
}
