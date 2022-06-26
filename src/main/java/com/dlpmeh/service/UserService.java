package com.dlpmeh.service;

import java.util.List;

import com.dlpmeh.config.model.User;
import com.dlpmeh.exception.UserException;

/**
 * Service interface for managing user-related operations.
 */
public interface UserService {

   public User findUserById(Long userId) throws UserException;

   public User findUserProfileByJwt(String jwtToken) throws UserException;

   public User updateUser(Long userId, User req) throws UserException;

   public User followUser(Long userId, User user) throws UserException;

   public List<User> searchUser(String query);

   public User registerUser(User user) throws UserException;

}
