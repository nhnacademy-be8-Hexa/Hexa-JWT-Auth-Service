//
//package com.nhnacademy.hexajwtauthservice.controller.token;
//
//import com.nhnacademy.hexajwtauthservice.service.BlackListRefreshTokenService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.restdocs.RestDocumentationExtension;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@ExtendWith(RestDocumentationExtension.class)
//@WebMvcTest(controllers = RedisBlackListRefreshTokenController.class)
//@AutoConfigureMockMvc
//@AutoConfigureRestDocs
//public class RedisBlackListRefreshTokenControllerTest {
//
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private BlackListRefreshTokenService refreshTokenService;
//
//    @InjectMocks
//    private RedisBlackListRefreshTokenController redisBlackListRefreshTokenController;
//
//
//
//    @Test
//    void addToBlackListToken_shouldReturnCreated() throws Exception {
//        String refreshToken = "sample-refresh-token";
//
//        // Arrange: Mocking the service method
//        doNothing().when(refreshTokenService).addToBlackList(anyString());
//
//        // Act & Assert: Perform POST request and assert response status
//        mockMvc.perform(post("/api/auth/refreshTokenBlacklists")
//                        .header("Authorization", "Bearer " + refreshToken))
//                .andExpect(status().isCreated());
//
//        // Verify that the service method was called once
//        verify(refreshTokenService, times(1)).addToBlackList(refreshToken);
//    }
//
//    @Test
//    void addToBlackListToken_shouldReturnBadRequest_whenNoAuthorizationHeader() throws Exception {
//        // Act & Assert: Perform POST request without Authorization header
//        mockMvc.perform(post("/api/auth/refreshTokenBlacklists"))
//                .andExpect(status().isBadRequest())
//                .andDo(document("add-to-blacklist-no-header"));
//    }
//
//    @Test
//    void isTokenBlackListed_shouldReturnTrue() throws Exception {
//        String refreshToken = "sample-refresh-token";
//
//        // Arrange: Mocking the service method
//        when(refreshTokenService.isTokenBlackListed(anyString())).thenReturn(true);
//
//        // Act & Assert: Perform GET request and assert response body
//        mockMvc.perform(get("/api/auth/refreshTokenBlacklists")
//                        .header("Authorization", "Bearer " + refreshToken))
//                .andExpect(status().isOk());
//
//        // Verify that the service method was called once
//        verify(refreshTokenService, times(1)).isTokenBlackListed(refreshToken);
//    }
//
//    @Test
//    void isTokenBlackListed_shouldReturnFalse() throws Exception {
//        String refreshToken = "sample-refresh-token";
//
//        // Arrange: Mocking the service method
//        when(refreshTokenService.isTokenBlackListed(anyString())).thenReturn(false);
//
//        // Act & Assert: Perform GET request and assert response body
//        mockMvc.perform(get("/api/auth/refreshTokenBlacklists")
//                        .header("Authorization", "Bearer " + refreshToken))
//                .andExpect(status().isOk());
//
//        // Verify that the service method was called once
//        verify(refreshTokenService, times(1)).isTokenBlackListed(refreshToken);
//    }
//
//    @Test
//    void isTokenBlackListed_shouldReturnBadRequest_whenNoAuthorizationHeader() throws Exception {
//        // Act & Assert: Perform GET request without Authorization header
//        mockMvc.perform(get("/api/auth/refreshTokenBlacklists"))
//                .andExpect(status().isBadRequest())
//                .andDo(document("check-blacklisted-token-no-header"));
//    }
//
//    @Test
//    void removeFromBlackList_shouldReturnNoContent() throws Exception {
//        String refreshToken = "sample-refresh-token";
//
//        // Arrange: Mocking the service method
//        doNothing().when(refreshTokenService).removeFromBlackList(anyString());
//
//        // Act & Assert: Perform DELETE request and assert response status
//        mockMvc.perform(delete("/api/auth/refreshTokenBlacklists")
//                        .header("Authorization", "Bearer " + refreshToken))
//                .andExpect(status().isNoContent());
//
//        // Verify that the service method was called once
//        verify(refreshTokenService, times(1)).removeFromBlackList(refreshToken);
//    }
//
//    @Test
//    void removeFromBlackList_shouldReturnBadRequest_whenNoAuthorizationHeader() throws Exception {
//        // Act & Assert: Perform DELETE request without Authorization header
//        mockMvc.perform(delete("/api/auth/refreshTokenBlacklists"))
//                .andExpect(status().isBadRequest())
//                .andDo(document("remove-from-blacklist-no-header"));
//    }
//}
//

package com.nhnacademy.hexajwtauthservice.controller.token;

import com.nhnacademy.hexajwtauthservice.service.BlackListRefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = RedisBlackListRefreshTokenController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class RedisBlackListRefreshTokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BlackListRefreshTokenService refreshTokenService;

    @InjectMocks
    private RedisBlackListRefreshTokenController redisBlackListRefreshTokenController;

    @Test
    void addToBlackListToken_shouldReturnCreated() throws Exception {
        String refreshToken = "sample-refresh-token";

        // Arrange: Mocking the service method
        doNothing().when(refreshTokenService).addToBlackList(anyString());

        // Act & Assert: Perform POST request and assert response status
        mockMvc.perform(post("/api/auth/refreshTokenBlacklists")
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isCreated())
                .andDo(document("add-to-blacklist", requestHeaders(  // 헤더 문서화
                        headerWithName("Authorization").description("리프레시 토큰")
                )));

        // Verify that the service method was called once
        verify(refreshTokenService, times(1)).addToBlackList(refreshToken);
    }

    @Test
    void addToBlackListToken_shouldReturnBadRequest_whenNoAuthorizationHeader() throws Exception {
        // Act & Assert: Perform POST request without Authorization header
        mockMvc.perform(post("/api/auth/refreshTokenBlacklists"))
                .andExpect(status().isBadRequest())
                .andDo(document("add-to-blacklist-no-header"));
    }

    @Test
    void isTokenBlackListed_shouldReturnTrue() throws Exception {
        String refreshToken = "sample-refresh-token";

        // Arrange: Mocking the service method
        when(refreshTokenService.isTokenBlackListed(anyString())).thenReturn(true);

        // Act & Assert: Perform GET request and assert response body
        mockMvc.perform(get("/api/auth/refreshTokenBlacklists")
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isOk())
                .andDo(document("check-blacklisted-token",requestHeaders(  // 헤더 문서화
                        headerWithName("Authorization").description("리프레시 토큰"))));

        // Verify that the service method was called once
        verify(refreshTokenService, times(1)).isTokenBlackListed(refreshToken);
    }

    @Test
    void isTokenBlackListed_shouldReturnFalse() throws Exception {
        String refreshToken = "sample-refresh-token";

        // Arrange: Mocking the service method
        when(refreshTokenService.isTokenBlackListed(anyString())).thenReturn(false);

        // Act & Assert: Perform GET request and assert response body
        mockMvc.perform(get("/api/auth/refreshTokenBlacklists")
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isOk())
                .andDo(document("check-blacklisted-token-false",requestHeaders(  // 헤더 문서화
                        headerWithName("Authorization").description("리프레시 토큰"))));

        // Verify that the service method was called once
        verify(refreshTokenService, times(1)).isTokenBlackListed(refreshToken);
    }

    @Test
    void isTokenBlackListed_shouldReturnBadRequest_whenNoAuthorizationHeader() throws Exception {
        // Act & Assert: Perform GET request without Authorization header
        mockMvc.perform(get("/api/auth/refreshTokenBlacklists"))
                .andExpect(status().isBadRequest())
                .andDo(document("check-blacklisted-token-no-header"));
    }

    @Test
    void removeFromBlackList_shouldReturnNoContent() throws Exception {
        String refreshToken = "sample-refresh-token";

        // Arrange: Mocking the service method
        doNothing().when(refreshTokenService).removeFromBlackList(anyString());

        // Act & Assert: Perform DELETE request and assert response status
        mockMvc.perform(delete("/api/auth/refreshTokenBlacklists")
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isNoContent())
                .andDo(document("remove-from-blacklist",requestHeaders(  // 헤더 문서화
                        headerWithName("Authorization").description("리프레시 토큰"))));

        // Verify that the service method was called once
        verify(refreshTokenService, times(1)).removeFromBlackList(refreshToken);
    }

}
