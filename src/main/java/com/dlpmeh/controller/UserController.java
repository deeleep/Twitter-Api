package com.dlpmeh.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlpmeh.config.model.User;
import com.dlpmeh.dto.UserDto;
import com.dlpmeh.dto.mapper.UserDtoMapper;
import com.dlpmeh.exception.UserException;
import com.dlpmeh.service.UserService;
import com.dlpmeh.util.UserUtil;

/**
 * Exposes user-related operations: profile retrieval, search, updates, and
 * follow management.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	/**
	 * GET /profile : Return the authenticated user’s profile.
	 *
	 * @param jwtToken Bearer token header
	 * @return current user’s UserDto
	 * @throws UserException if authentication fails
	 */
	@GetMapping("/profile")
	public ResponseEntity<UserDto> getUserProfile(
			@RequestHeader("Authorization") String jwtToken)
			throws UserException {

		logger.info("Fetching profile for authenticated user.");
		User user = userService.findUserProfileByJwt(jwtToken);
		logger.info("User identified: {}", user.getEmail());

		UserDto userDto = UserDtoMapper.toUserDto(user);
		userDto.setReq_user(true);

		return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
	}

	/**
	 * GET /{userId} : Return details for a specific user.
	 *
	 * @param userId   target user ID
	 * @param jwtToken Bearer token header
	 * @return UserDto with follow status
	 * @throws UserException if authentication fails or user not found
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(
			@PathVariable Long userId,
			@RequestHeader("Authorization") String jwtToken)
			throws UserException {

		logger.info("Fetching user details for userId={}", userId);
		User reqUser = userService.findUserProfileByJwt(jwtToken);
		logger.info("Requesting user: {}", reqUser.getEmail());

		User user = userService.findUserById(userId);

		UserDto userDto = UserDtoMapper.toUserDto(user);
		userDto.setReq_user(UserUtil.isReqUser(reqUser, user));
		userDto.setFollowed(UserUtil.isFollowedByReqUser(reqUser, user));

		return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
	}

	/**
	 * GET /search : Search users by query string.
	 *
	 * @param query    search term
	 * @param jwtToken Bearer token header
	 * @return list of matching UserDto
	 * @throws UserException if authentication fails
	 */
	@GetMapping("/search")
	public ResponseEntity<List<UserDto>> searchUser(
			@RequestParam String query,
			@RequestHeader("Authorization") String jwtToken)
			throws UserException {

		logger.info("Searching users with query='{}'", query);
		User reqUser = userService.findUserProfileByJwt(jwtToken);
		logger.info("Search requested by user: {}", reqUser.getEmail());

		List<User> users = userService.searchUser(query);
		logger.info("Found {} users matching query='{}'", users.size(), query);

		List<UserDto> userDtos = UserDtoMapper.toUserDtos(users);
		return new ResponseEntity<>(userDtos, HttpStatus.ACCEPTED);
	}

	/**
	 * PUT /update : Update authenticated user’s profile.
	 *
	 * @param req      updated fields
	 * @param jwtToken Bearer token header
	 * @return updated UserDto
	 * @throws UserException if authentication fails or update fails
	 */
	@PutMapping("/update")
	public ResponseEntity<UserDto> updateUser(
			@RequestBody User req,
			@RequestHeader("Authorization") String jwtToken)
			throws UserException {

		logger.info("Received request to update user profile.");

		User reqUser = userService.findUserProfileByJwt(jwtToken);
		logger.info("User identified: {}", reqUser.getEmail());

		User updatedUser = userService.updateUser(reqUser.getId(), req);
		logger.info("User profile updated successfully for userId={}", updatedUser.getId());

		UserDto userDto = UserDtoMapper.toUserDto(updatedUser);
		return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
	}

	/**
	 * PUT /{userId}/follow : Follow a user.
	 *
	 * @param userId   ID to follow
	 * @param jwtToken Bearer token header
	 * @return followed user’s UserDto
	 * @throws UserException if authentication fails or action fails
	 */
	@PutMapping("/{userId}/follow")
	public ResponseEntity<UserDto> followUser(
			@PathVariable Long userId,
			@RequestHeader("Authorization") String jwtToken)
			throws UserException {

		logger.info("Follow request: targetUserId={}", userId);
		// Get the requesting user (the one who is logged in)
		User reqUser = userService.findUserProfileByJwt(jwtToken);
		logger.info("Requesting user: {}", reqUser.getEmail());

		// Perform the follow action
		User followedUser = userService.followUser(userId, reqUser);
		logger.info("UserId={} followed by userId={}", userId, reqUser.getId());

		UserDto userDto = UserDtoMapper.toUserDto(followedUser);
		userDto.setFollowed(UserUtil.isFollowedByReqUser(reqUser, followedUser));

		// Return response
		return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
	}

	/**
	 * GET /{userId}/followers : List followers of a user.
	 *
	 * @param userId   target user ID
	 * @param jwtToken Bearer token header
	 * @return list of UserDto
	 * @throws UserException if authentication fails
	 */
	@GetMapping("/{userId}/followers")
	public ResponseEntity<List<UserDto>> getFollowers(
			@PathVariable Long userId,
			@RequestHeader("Authorization") String jwtToken) throws UserException {

		logger.info("Fetching followers for userId={}", userId);

		// Identify who is making the request (for potential permission checks or
		// logging)
		User reqUser = userService.findUserProfileByJwt(jwtToken);
		logger.info("Requesting user: {}", reqUser.getEmail());

		// Get target user and their followers
		User targetUser = userService.findUserById(userId);
		List<User> followers = targetUser.getFollowers();

		// Map to DTOs
		List<UserDto> followerDtos = UserDtoMapper.toUserDtos(followers);

		return new ResponseEntity<>(followerDtos, HttpStatus.OK);
	}

	/**
	 * GET /{userId}/following : List users that a user is following.
	 *
	 * @param userId   target user ID
	 * @param jwtToken Bearer token header
	 * @return list of UserDto
	 * @throws UserException if authentication fails
	 */
	@GetMapping("/{userId}/following")
	public ResponseEntity<List<UserDto>> getFollowing(@PathVariable Long userId,
			@RequestHeader("Authorization") String jwtToken) throws UserException {

		logger.info("Fetching following users for userId={}", userId);

		// Authenticate requesting user from JWT (optional: you may or may not need this
		// here)
		User reqUser = userService.findUserProfileByJwt(jwtToken);
		logger.info("Requesting user: {}", reqUser.getEmail());

		// Find user by id
		User user = userService.findUserById(userId);

		// Get users this user is following
		List<User> following = user.getFollowingUsers();

		// Map to DTOs
		List<UserDto> followingDtos = UserDtoMapper.toUserDtos(following);

		// Return response
		return new ResponseEntity<>(followingDtos, HttpStatus.OK);
	}

}
