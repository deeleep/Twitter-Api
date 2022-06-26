package com.dlpmeh.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dlpmeh.config.JwtTokenGenerator;
import com.dlpmeh.config.model.User;
import com.dlpmeh.config.repository.UserRepository;
import com.dlpmeh.exception.UserException;

@Service
public class UserServicesImplementation implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServicesImplementation.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenGenerator jwtTokenGenerator;

	@Override
	public User findUserById(Long userId) throws UserException {
		logger.info("Fetching user by ID: {}", userId);

		User user = userRepository.findById(userId)
				.orElseThrow(() -> {
					logger.warn("User not found with ID: {}", userId);
					return new UserException("User not found with id " + userId);
				});

		logger.info("User found: id={}, email={}", user.getId(), user.getEmail());
		return user;
	}

	@Override
	public User findUserProfileByJwt(String jwtToken) throws UserException {

		logger.info("Decoding JWT to get user email");
		String email = jwtTokenGenerator.getEmailFromToken(jwtToken);

		if (email == null) {
			logger.warn("Invalid JWT token: no email found");

			throw new UserException("Invalid JWT token. Email not found.");
		}

		logger.info("Email extracted from JWT: {}", email);
		User user = userRepository.findByEmail(email);

		if (user == null) {
			logger.warn("No user found for email: {}", email);
			throw new UserException("No user exists with the email address:" + email);
		}

		logger.info("User authenticated via JWT: {}", email);
		return user;
	}

	@Override
	public User updateUser(Long userId, User request) throws UserException {

		logger.info("Updating profile for userId={}", userId);

		User user = findUserById(userId);

		if (request.getFullName() != null) {
			user.setFullName(request.getFullName());
		}

		if (request.getImage() != null) {
			user.setImage(request.getImage());
		}

		if (request.getBackgroundImage() != null) {
			user.setBackgroundImage(request.getBackgroundImage());
		}

		if (request.getDateOfBirth() != null) {
			user.setDateOfBirth(request.getDateOfBirth());
		}

		if (request.getLocation() != null) {
			user.setLocation(request.getLocation());
		}

		if (request.getBio() != null) {
			user.setBio(request.getBio());
		}

		if (request.getWebsite() != null) {
			user.setWebsite(request.getWebsite());
		}

		logger.info("User profile updated: userId={}", userId);
		return userRepository.save(user);

	}

	@Override
	public User followUser(Long userId, User user) throws UserException {
		logger.info("Follow/unfollow request from userId={} to userId={}", user.getId(), userId);

		// Prevent user from following themselves
		if (user.getId().equals(userId)) {
			throw new UserException("You cannot follow yourself.");
		}

		// Fetch the user to follow/unfollow
		User followToUser = findUserById(userId);

		if (user.getFollowingUsers().contains(followToUser) && followToUser.getFollowers().contains(user)) {
			// Unfollow logic
			user.getFollowingUsers().remove(followToUser);
			followToUser.getFollowers().remove(user);
			logger.info("User unfollowed: followerId={}, unfollowedUserId={}", user.getId(), userId);

		} else {
			// Follow logic
			user.getFollowingUsers().add(followToUser);
			followToUser.getFollowers().add(user);
			logger.info("User followed: followerId={}, followedUserId={}", user.getId(), userId);

		}

		userRepository.save(followToUser);
		return userRepository.save(user);

	}

	@Override
	public List<User> searchUser(String query) {
		logger.info("Searching users with query='{}'", query);

		List<User> users = userRepository.searchUser(query);
		logger.info("Found {} users matching query='{}'", users.size(), query);
		return users;

	}

	public User registerUser(User user) throws UserException {
		logger.info("Registering new user: email={}", user.getEmail());

		if (user.getEmail() == null || user.getPassword() == null) {
			logger.warn("Registration failed — missing email or password");

			throw new UserException("Email and password are required.");
		}

		// check if user already exists
		if (userRepository.findByEmail(user.getEmail()) != null) {
			logger.warn("Registration failed — user already exists: {}", user.getEmail());

			throw new UserException("User already exists with this email.");
		}

		User saved = userRepository.save(user);
		logger.info("User registered successfully: id={}, email={}", saved.getId(), saved.getEmail());
		return saved;

	}

}
