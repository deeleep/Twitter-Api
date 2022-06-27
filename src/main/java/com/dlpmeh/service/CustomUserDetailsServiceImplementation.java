package com.dlpmeh.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dlpmeh.config.model.User;
import com.dlpmeh.config.repository.UserRepository;

@Service
public class CustomUserDetailsServiceImplementation implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsServiceImplementation.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username (email): {}", username);

        User user = userRepository.findByEmail(username);

        if (user == null || user.isLoginWithGoogle()) {
            logger.warn("User not found or login via Google is enabled for email: {}", username);
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        logger.info("User found: {} — preparing UserDetails", username);

        List<GrantedAuthority> authorities = new ArrayList<>();

        // You can enable this if you want to assign roles:
        // authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}
