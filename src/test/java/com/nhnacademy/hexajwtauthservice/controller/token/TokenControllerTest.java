package com.nhnacademy.hexajwtauthservice.controller.token;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.hexajwtauthservice.service.JwtService;
import com.nhnacademy.hexajwtauthservice.service.BlackListRefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = TokenController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private BlackListRefreshTokenService blackListRefreshTokenService;

    @InjectMocks
    private TokenController tokenController;

    @Autowired
    private ObjectMapper objectMapper;

    private String refreshToken;
    private String userId = "testUser";
    private String userRole = "USER";

    @BeforeEach
    public void setUp() {
        refreshToken = "valid-refresh-token";
    }

    // 기존 정상 토큰 발급 테스트
    @Test
    public void reissueAccessRefreshToken_Success() throws Exception {
        String newAccessToken = "new-access-token";
        String newRefreshToken = "new-refresh-token";

        when(jwtService.extractUserIdFromJwt(refreshToken, false)).thenReturn(userId);
        when(jwtService.extractRoleFromJwt(refreshToken, false)).thenReturn("ROLE_" + userRole);
        when(jwtService.generateAccessToken(userId, userRole)).thenReturn(newAccessToken);
        when(jwtService.generateRefreshToken(userId, userRole)).thenReturn(newRefreshToken);
        doNothing().when(blackListRefreshTokenService).addToBlackList(refreshToken);

        mockMvc.perform(post("/api/auth/reissue")
                        .header("Authorization", "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(newAccessToken))
                .andExpect(jsonPath("$.refreshToken").value(newRefreshToken))
                .andDo(document("reissue-token",
                        responseFields(
                                fieldWithPath("accessToken").description("새로 발급된 Access Token"),
                                fieldWithPath("refreshToken").description("새로 발급된 Refresh Token")
                        )));
    }

}
