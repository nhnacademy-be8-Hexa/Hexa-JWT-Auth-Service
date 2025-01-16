package com.nhnacademy.hexajwtauthservice.credentials;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class DatabaseCredentials {

    private Map<String, String> credentialsMap = new HashMap<>();

    // 생성자: info는 JSON 형식의 문자열
    public DatabaseCredentials(String info) {
        parseJsonToMap(info);
    }

    // info(JSON 형식의 문자열)을 Map으로 변환하는 함수
    private void parseJsonToMap(String info) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(info); // info를 JSON으로 변환

            // JSON의 모든 필드와 그 값을 Map에 저장
            jsonNode.fields().forEachRemaining(field -> {
                credentialsMap.put(field.getKey(), field.getValue().asText());
            });
        } catch (Exception e) {
            log.error("JSON 변환 중 에러 발생: {}",e.getMessage(),e);
        }
    }

    // credentialsMap 반환 함수
    public Map<String, String> getCredentialsMap() {
        return credentialsMap;
    }
}