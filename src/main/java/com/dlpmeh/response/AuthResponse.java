package com.dlpmeh.response;

import lombok.AllArgsConstructor;

import com.dlpmeh.dto.AuthDataDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AuthResponse {

    private boolean success;
    private String message;
    private AuthDataDto data;

}
