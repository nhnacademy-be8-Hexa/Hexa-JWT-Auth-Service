package com.nhnacademy.hexajwtauthservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private String memberId;
    private String memberPassword;
    private String memberName;
    private String memberNumber;
    private LocalDate memberBirthAt;
    private LocalDate memberCreatedAt;
    private LocalDateTime memberLastLoginAt;
    private Role memberRole;
    private Rating rating;
    private MemberStatus memberStatus;

}
