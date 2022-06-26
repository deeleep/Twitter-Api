package com.dlpmeh.response;

import com.dlpmeh.config.model.User;

import lombok.Data;

@Data
public class LoginResponse {

    private String jwt;
    private User user;

}
