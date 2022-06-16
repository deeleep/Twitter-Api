package com.dlpmeh.dto;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserDto {

    private Long id;
    private String fullName;
    private String email;

    private String image;
    private String location;
    private String website;
    private String birthDate;
    private String mobile;
    private String backgroundImage;
    private String bio;

    private boolean req_user;
    private boolean loginWithGoogle;
    private boolean followed; // Whether current user follows this user
    private boolean isVerified; // User verification status

    private List<UserDto> followers = new ArrayList<>(); // List of users following this user
    private List<UserDto> following = new ArrayList<>(); // List of users this user is following

}
