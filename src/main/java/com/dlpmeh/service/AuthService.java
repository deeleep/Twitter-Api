package com.dlpmeh.service;

import com.dlpmeh.request.LoginRequest;
import com.dlpmeh.response.LoginResponse;

/**
 * Service interface for handling user authentication.
 */
public interface AuthService {
    LoginResponse loginUser(LoginRequest request) throws Exception;
}
