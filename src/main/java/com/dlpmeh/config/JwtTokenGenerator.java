package com.dlpmeh.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 * Generates and parses JWTs for authenticated users.
 */
@Service
public class JwtTokenGenerator {

        // Static secret key for signing JWT tokens
        private static final Logger logger = LoggerFactory.getLogger(JwtTokenGenerator.class);

        private final SecretKey key = Keys.hmacShaKeyFor(JwtConstants.SECRET_KEY.getBytes());

        /**
         * Generate a JWT for the given authentication.
         *
         * @param auth authenticated user context
         * @return signed JWT token
         */
        public String generateToken(Authentication auth) {
                String jwtToken = Jwts.builder()
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(new Date().getTime() + 86400000)) // Token expires in 1 day
                                .claim("email", auth.getName()) // Set email claim from authenticated user
                                .signWith(key)
                                .compact();

                return jwtToken;
        }

        /**
         * Extracts the email from the JWT token.
         * 
         * @param jwtToken the JWT token string
         * @return the email contained in the token
         */
        public String getEmailFromToken(String jwtToken) {

                jwtToken = jwtToken.substring(7);
                Claims claims = Jwts.parserBuilder()
                                .setSigningKey(key)
                                .build()
                                .parseClaimsJws(jwtToken)
                                .getBody();

                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));
                logger.debug("Authorities: {}", authorities);

                return email;
        }
}
