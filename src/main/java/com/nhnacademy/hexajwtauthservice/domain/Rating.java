package com.nhnacademy.hexajwtauthservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Rating {
    private Long ratingId;
    private String ratingName;
    private Integer ratingPercent;

}
