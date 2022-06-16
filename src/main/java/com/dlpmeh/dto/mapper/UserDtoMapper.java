package com.dlpmeh.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.dlpmeh.config.model.User;
import com.dlpmeh.dto.UserDto;

public class UserDtoMapper {

    public static UserDto toUserDto(User user) {
        if (user == null)
            return null;

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setImage(user.getImage());
        userDto.setBackgroundImage(user.getBackgroundImage());
        userDto.setBio(user.getBio());
        userDto.setBirthDate(user.getDateOfBirth());
        userDto.setWebsite(user.getWebsite());
        userDto.setFollowers(toUserDtos(user.getFollowers()));
        userDto.setFollowing(toUserDtos(user.getFollowingUsers()));
        userDto.setLoginWithGoogle(user.isLoginWithGoogle());
        userDto.setLocation(user.getLocation());

        // userDto.setMobileNumber(user.getMobileNumber());
        // userDto.setVerified(false); // HardCoded for now, change if needed
        // Set other fields if needed

        return userDto;
    }

    public static List<UserDto> toUserDtos(List<User> followers) {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : followers) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            userDto.setFullName(user.getFullName());
            userDto.setImage(user.getImage());
            userDtos.add(userDto);
        }
        return userDtos;
    }

}
