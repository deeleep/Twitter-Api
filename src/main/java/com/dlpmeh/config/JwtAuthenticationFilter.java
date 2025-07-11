package com.dlpmeh.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filters incoming requests to validate JWT tokens from the Authorization
 * header.
 * On success, sets the authenticated user in the security context.
 * 
 * Author: D Mehata
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwtToken = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);

            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConstants.SECRET_KEY.getBytes());

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody();

                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                System.out.println("Invalid JWT Token: " + e.getMessage());
                throw new BadCredentialsException("Invalid JWT Token");

            }
        }

        filterChain.doFilter(request, response);
    }
}
