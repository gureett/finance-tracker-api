package com.kharod.finance_tracker.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "your-super-secret-key-that-is-at-least-32-characters-long");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        String token = jwtUtil.generateToken("john@example.com");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractEmail_shouldReturnCorrectEmail() {
        String token = jwtUtil.generateToken("john@example.com");
        String email = jwtUtil.extractEmail(token);
        assertEquals("john@example.com", email);
    }

    @Test
    void isTokenValid_shouldReturnTrue_whenTokenIsValid() {
        String token = jwtUtil.generateToken("john@example.com");
        assertTrue(jwtUtil.isTokenValid(token, "john@example.com"));
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenEmailDoesNotMatch() {
        String token = jwtUtil.generateToken("john@example.com");
        assertFalse(jwtUtil.isTokenValid(token, "jane@example.com"));
    }
}