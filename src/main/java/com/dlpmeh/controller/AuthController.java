package com.dlpmeh.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlpmeh.config.JwtTokenGenerator;
import com.dlpmeh.config.model.User;
import com.dlpmeh.config.model.Verification;
import com.dlpmeh.config.repository.UserRepository;
import com.dlpmeh.dto.AuthDataDto;
import com.dlpmeh.dto.SignInUserDto;
import com.dlpmeh.dto.SignUpUserDto;
import com.dlpmeh.exception.UserException;
import com.dlpmeh.response.AuthResponse;
import com.dlpmeh.service.CustomUserDetailsServiceImplementation;

import lombok.Data;

/**
 * Provides signup and signin endpoints under /auth.
 */
@Data
@RestController
@RequestMapping("/auth")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
  private static final LocalDateTime NOW = LocalDateTime.now().withNano(0);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenGenerator jwtTokenGenerator;

  @Autowired
  private CustomUserDetailsServiceImplementation customUserDetailsServiceImplementation;

  /**
   * POST /auth/signup : Register a new user and issue a JWT.
   *
   * @param user registration payload
   * @return AuthResponse with user info and token
   * @throws UserException if email is already in use
   */
  @PostMapping("/signup")
  public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {

    String email = user.getEmail();
    String password = user.getPassword();
    String fullName = user.getFullName();
    String dateOfString = user.getDateOfBirth();

    logger.info("Signup request received for email: {}", email);
    User isEmailExist = userRepository.findByEmail(email);
    if (isEmailExist != null) {
      logger.warn("Signup failed — Email already in use: {}", email);
      throw new UserException("Email is already used with another account");
    }

    User createdUser = new User();
    createdUser.setEmail(email);
    createdUser.setFullName(fullName);
    createdUser.setPassword(passwordEncoder.encode(password));
    createdUser.setDateOfBirth(dateOfString);
    createdUser.setVerification(new Verification());
    createdUser.setLoginWithGoogle(false);
    createdUser.setCreatedAt(NOW);

    User savedUser = userRepository.save(createdUser);
    logger.info("User created successfully: {}", savedUser.getEmail());

    Authentication authentication = new UsernamePasswordAuthenticationToken(email, savedUser);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = jwtTokenGenerator.generateToken(authentication);
    logger.info("JWT token generated for signup user: {}", savedUser.getEmail());

    SignUpUserDto userDto = new SignUpUserDto();
    userDto.setId(savedUser.getId());
    userDto.setFullName(savedUser.getFullName());
    userDto.setEmail(savedUser.getEmail());
    userDto.setEmailVerified(false);
    userDto.setCreatedAt(savedUser.getCreatedAt());

    AuthDataDto dataDto = new AuthDataDto();
    dataDto.setJwtToken(token);
    dataDto.setUser(userDto);

    AuthResponse response = new AuthResponse();
    response.setSuccess(true);
    response.setMessage("User registered successfully");
    response.setData(dataDto);

    return new ResponseEntity<AuthResponse>(response, HttpStatus.CREATED);
  }

  /**
   * POST /auth/signin : Authenticate credentials and issue a JWT.
   *
   * @param user signin payload
   * @return AuthResponse with user info and token
   */
  @PostMapping("/signin")
  public ResponseEntity<AuthResponse> signIn(@RequestBody User user) {

    String username = user.getEmail();
    String password = user.getPassword();

    logger.info("Signin request received for email: {}", username);
    Authentication authentication = authenticate(username, password);

    String token = jwtTokenGenerator.generateToken(authentication);
    logger.info("JWT token generated for signin user: {}", username);

    User savedUser = userRepository.findByEmail(username);
    savedUser.setLoginAt(NOW);
    userRepository.save(savedUser);

    SignInUserDto userDto = new SignInUserDto();
    userDto.setId(savedUser.getId());
    userDto.setFullName(savedUser.getFullName());
    userDto.setEmail(savedUser.getEmail());
    // userDto.setLoginAt(savedUser.getLoginAt());

    AuthDataDto dataDto = new AuthDataDto();
    dataDto.setJwtToken(token);
    dataDto.setUser(userDto);

    AuthResponse response = new AuthResponse();
    response.setSuccess(true);
    response.setMessage("User signed in successfully");
    response.setData(dataDto);

    return new ResponseEntity<AuthResponse>(response, HttpStatus.ACCEPTED);

  }

  /**
   * Validate credentials against stored user details.
   *
   * @param username email
   * @param password plaintext password
   * @return Authentication on success
   * @throws BadCredentialsException if invalid
   */
  private Authentication authenticate(String username, String password) {
    logger.debug("Authenticating user: {}", username);
    UserDetails userDetails = customUserDetailsServiceImplementation.loadUserByUsername(username);

    if (userDetails == null) {
      logger.error("User not found: {}", username);
      throw new BadCredentialsException("Invalid Username");
    }

    if (!passwordEncoder.matches(password, userDetails.getPassword())) {
      logger.error("Invalid credentials for user: {}", username);
      throw new BadCredentialsException("Invalid Username or password");
    }

    logger.info("Authentication successful for user: {}", username);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

  }

}
