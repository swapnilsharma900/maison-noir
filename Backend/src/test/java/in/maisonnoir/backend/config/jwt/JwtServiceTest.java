package in.maisonnoir.backend.config.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String secretKey = "TW9pc29uTm9pclNlY3JldEtleUZvckpXVEF1dGhlbnRpY2F0aW9uMjAyNg=="; // From application.properties
    private final long expiration = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", expiration);
    }

    @Test
    void testGenerateAndValidateToken() {
        UserDetails userDetails = User.builder()
                .username("test@maisonnoir.in")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());

        String username = jwtService.extractUsername(token);
        assertEquals("test@maisonnoir.in", username);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testTokenValidationWithWrongUser() {
        UserDetails userDetails = User.builder()
                .username("test@maisonnoir.in")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        UserDetails wrongUser = User.builder()
                .username("wrong@maisonnoir.in")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        String token = jwtService.generateToken(userDetails);
        assertFalse(jwtService.isTokenValid(token, wrongUser));
    }
}
