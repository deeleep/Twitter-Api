package com.dlpmeh.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlpmeh.config.model.Like;
import com.dlpmeh.config.model.User;
import com.dlpmeh.dto.LikeDto;
import com.dlpmeh.dto.mapper.LikeDtoMapper;
import com.dlpmeh.exception.TweetException;
import com.dlpmeh.exception.UserException;
import com.dlpmeh.service.LikeService;
import com.dlpmeh.service.UserService;

/**
 * Exposes endpoints to like tweets and retrieve likes.
 */
@RestController
@RequestMapping("/api")
public class LikeController {

	private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

	@Autowired
	private LikeService likeService;

	@Autowired
	private UserService userService;

	/**
	 * POST /{tweetId}/likes : Like a tweet.
	 *
	 * @param tweetId  ID of the tweet to like
	 * @param jwtToken Bearer token header
	 * @return the created LikeDto
	 * @throws UserException  if authentication fails
	 * @throws TweetException if like operation fails
	 */
	@PostMapping("/{tweetId}/likes")
	public ResponseEntity<LikeDto> likeTweet(
			@PathVariable Long tweetId,
			@RequestHeader("Authorization") String jwt)
			throws UserException, TweetException {

		logger.info("User requested to like tweet (tweetId={})", tweetId, jwt);

		User user = userService.findUserProfileByJwt(jwt);
		logger.info("User found for likeTweet: {}", user.getEmail());

		Like like = likeService.likeTweet(tweetId, user);
		logger.info("Tweet liked: tweetId={}, userId={}", tweetId, user.getId());

		LikeDto likeDto = LikeDtoMapper.toLikeDto(like, user);
		return new ResponseEntity<>(likeDto, HttpStatus.CREATED);
	}

	/**
	 * GET /{tweetId}/likes : Retrieve all likes for a tweet.
	 *
	 * @param tweetId  ID of the tweet
	 * @param jwtToken Bearer token header
	 * @return list of LikeDto
	 * @throws UserException  if authentication fails
	 * @throws TweetException if retrieval fails
	 */
	@PostMapping("/tweet/{tweetId}")
	public ResponseEntity<List<LikeDto>> getAllLikes(
			@PathVariable Long tweetId,
			@RequestHeader("Authorization") String jwt)
			throws UserException, TweetException {

		logger.info("Fetching likes: tweetId={}, authorizationToken={}", tweetId, jwt);

		User user = userService.findUserProfileByJwt(jwt);
		logger.info("User found for getAllLikes: {}", user.getEmail());

		List<Like> likes = likeService.getAllLikes(tweetId);
		logger.info("Fetched {} likes for tweetId={}", likes.size(), tweetId);

		List<LikeDto> likeDtos = LikeDtoMapper.toLikeDtos(likes, user);
		return new ResponseEntity<>(likeDtos, HttpStatus.CREATED);
	}

}
