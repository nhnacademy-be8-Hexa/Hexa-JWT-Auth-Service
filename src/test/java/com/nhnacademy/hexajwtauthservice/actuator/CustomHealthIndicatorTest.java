package com.nhnacademy.hexajwtauthservice.actuator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomHealthIndicatorTest {

    private ApplicationStatus applicationStatus;
    private CustomHealthIndicator customHealthIndicator;

    @BeforeEach
    void setUp() {
        // Mockito를 사용하여 ApplicationStatus mock 객체 생성
        applicationStatus = mock(ApplicationStatus.class);
        customHealthIndicator = new CustomHealthIndicator(applicationStatus);
    }

    @Test
    void testHealthDownWhenStatusIsFalse() {
        // applicationStatus.getStatus()가 false일 때
        when(applicationStatus.getStatus()).thenReturn(false);

        // health() 호출 후 결과 검증
        Health health = customHealthIndicator.health();
        assertEquals("DOWN", health.getStatus().toString());
        assertNull(health.getDetails().get("service"));
    }

    @Test
    void testHealthUpWhenStatusIsTrue() {
        // applicationStatus.getStatus()가 true일 때
        when(applicationStatus.getStatus()).thenReturn(true);

        // health() 호출 후 결과 검증
        Health health = customHealthIndicator.health();
        assertEquals("UP", health.getStatus().toString());
        assertEquals("start", health.getDetails().get("service"));
    }
}