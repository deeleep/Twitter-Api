package com.dlpmeh.dto;

import lombok.Data;

@Data
public class AuthDataDto {

    private String jwtToken;
    private UserDto user;
}