package com.nhnacademy.hexajwtauthservice.controller.token;



import com.nhnacademy.hexajwtauthservice.service.BlackListRefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class RedisBlackListRefreshTokenControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BlackListRefreshTokenService refreshTokenService;

    @InjectMocks
    private RedisBlackListRefreshTokenController redisBlackListRefreshTokenController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(redisBlackListRefreshTokenController).build();
    }

    @Test
    void addToBlackListToken_shouldReturnCreated() throws Exception {
        String refreshToken = "sample-refresh-token";

        // Arrange: Mocking the service method
        doNothing().when(refreshTokenService).addToBlackList(anyString());

        // Act & Assert: Perform POST request and assert response status
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/refreshTokenBlacklists")
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Verify that the service method was called once
        verify(refreshTokenService, times(1)).addToBlackList(refreshToken);
    }

    @Test
    void addToBlackListToken_shouldReturnBadRequest_whenNoAuthorizationHeader() throws Exception {
        // Act & Assert: Perform POST request without Authorization header
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/refreshTokenBlacklists"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void isTokenBlackListed_shouldReturnTrue() throws Exception {
        String refreshToken = "sample-refresh-token";

        // Arrange: Mocking the service method
        when(refreshTokenService.isTokenBlackListed(anyString())).thenReturn(true);

        // Act & Assert: Perform GET request and assert response body
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/refreshTokenBlacklists")
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));

        // Verify that the service method was called once
        verify(refreshTokenService, times(1)).isTokenBlackListed(refreshToken);
    }

    @Test
    void isTokenBlackListed_shouldReturnFalse() throws Exception {
        String refreshToken = "sample-refresh-token";

        // Arrange: Mocking the service method
        when(refreshTokenService.isTokenBlackListed(anyString())).thenReturn(false);

        // Act & Assert: Perform GET request and assert response body
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/refreshTokenBlacklists")
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));

        // Verify that the service method was called once
        verify(refreshTokenService, times(1)).isTokenBlackListed(refreshToken);
    }

    @Test
    void isTokenBlackListed_shouldReturnBadRequest_whenNoAuthorizationHeader() throws Exception {
        // Act & Assert: Perform GET request without Authorization header
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/refreshTokenBlacklists"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void removeFromBlackList_shouldReturnNoContent() throws Exception {
        String refreshToken = "sample-refresh-token";

        // Arrange: Mocking the service method
        doNothing().when(refreshTokenService).removeFromBlackList(anyString());

        // Act & Assert: Perform DELETE request and assert response status
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/auth/refreshTokenBlacklists")
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify that the service method was called once
        verify(refreshTokenService, times(1)).removeFromBlackList(refreshToken);
    }

    @Test
    void removeFromBlackList_shouldReturnBadRequest_whenNoAuthorizationHeader() throws Exception {
        // Act & Assert: Perform DELETE request without Authorization header
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/auth/refreshTokenBlacklists"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
