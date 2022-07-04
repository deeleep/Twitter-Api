
package com.dlpmeh.service;

import com.dlpmeh.config.model.User;
import com.dlpmeh.request.LoginRequest;
import com.dlpmeh.response.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Test
    public void testLoginUser() throws Exception {
        // First: register a user
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setUsername("loginTestUser");
        userService.registerUser(user); // ensure user exists

        // Then: perform login
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        LoginResponse response = authService.loginUser(request);

        assertNotNull(response.getJwt());
        assertEquals("test@example.com", response.getUser().getEmail());
    }
}
