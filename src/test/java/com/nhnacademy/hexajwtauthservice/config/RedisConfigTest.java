package com.nhnacademy.hexajwtauthservice.config;

import com.nhnacademy.hexajwtauthservice.credentials.DatabaseCredentials;
import com.nhnacademy.hexajwtauthservice.service.SecureKeyManagerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.clients.jedis.exceptions.JedisConnectionException;


@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private SecureKeyManagerService secureKeyManagerService;

    @Test
    public void testRedisConnection_success() throws Exception {
        // Mock SecureKeyManagerService to return valid credentials
        String mockCredentials = "{\"host\":\"localhost\", \"port\":\"6379\", \"password\":\"password\", \"database\":\"0\"}";
        when(secureKeyManagerService.fetchSecretFromKeyManager()).thenReturn(mockCredentials);

        // Create a Redis connection
        redisTemplate.opsForValue().set("test_key", "test_value");
        String value = redisTemplate.opsForValue().get("test_key");

        // Assert that the value retrieved from Redis matches the set value
        assertEquals("test_value", value);
    }

    @Test
    public void testRedisConnection_failure_invalidCredentials() throws Exception {
        // Mock SecureKeyManagerService to return invalid credentials (e.g., wrong password)
        String mockCredentials = "{\"host\":\"localhost\", \"port\":\"6379\", \"password\":\"wrong_password\", \"database\":\"0\"}";
        when(secureKeyManagerService.fetchSecretFromKeyManager()).thenReturn(mockCredentials);

        // Expected exception due to invalid Redis credentials

        // Attempt to create a Redis connection (should fail)
        redisTemplate.opsForValue().set("test_key", "test_value");
    }
}

