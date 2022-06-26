package com.dlpmeh.service;

import com.dlpmeh.config.JwtConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dlpmeh.config.model.User;
import com.dlpmeh.config.repository.UserRepository;
import com.dlpmeh.request.LoginRequest;
import com.dlpmeh.response.LoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Service implementation for handling user authentication.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public LoginResponse loginUser(LoginRequest request) throws Exception {
        logger.info("Login attempt received for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail());
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            logger.warn("Login failed for email: {}", request.getEmail());
            throw new Exception("Invalid email or password");
        }

        logger.info("User authenticated successfully: {}", user.getEmail());

        // Generate JWT token
        SecretKey key = Keys.hmacShaKeyFor(JwtConstants.SECRET_KEY.getBytes());

        String jwt = Jwts.builder()
                .claim("email", user.getEmail())
                .claim("authorities", "ROLE_USER")
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        logger.info("JWT token generated for user: {}", user.getEmail());

        LoginResponse response = new LoginResponse();
        response.setJwt(jwt);
        response.setUser(user);

        return response;
    }
}
