package com.nhnacademy.hexajwtauthservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponse {
    private String memberId;
    private String memberPassword;
    private String memberName;
    private String memberNumber;
    private LocalDate memberBirthAt;
    private LocalDate memberCreatedAt;
    private LocalDateTime memberLastLoginAt;
    private String memberRole;
    private String ratingId;
    private String statusId;
}
