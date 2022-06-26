package com.dlpmeh.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlpmeh.config.model.Tweet;
import com.dlpmeh.config.model.User;
import com.dlpmeh.dto.TweetDto;
import com.dlpmeh.dto.mapper.TweetDtoMapper;
import com.dlpmeh.exception.TweetException;
import com.dlpmeh.exception.UserException;
import com.dlpmeh.request.TweetReplyRequest;
import com.dlpmeh.response.ApiResponse;
import com.dlpmeh.service.TweetService;
import com.dlpmeh.service.UserService;

/**
 * Exposes endpoints to create, reply, retweet, fetch, and delete tweets.
 */
@RestController
@RequestMapping("/api/tweets")
public class TweetController {
	private static final Logger logger = LoggerFactory.getLogger(TweetController.class);

	@Autowired
	private TweetService tweetService;

	@Autowired
	private UserService userService;

	/**
	 * POST /create : Publish a new tweet.
	 *
	 * @param req      tweet payload
	 * @param jwtToken Bearer token
	 * @return the created TweetDto
	 * @throws UserException  if authentication fails
	 * @throws TweetException if creation fails
	 */
	@PostMapping("/create")
	public ResponseEntity<TweetDto> createTweet(
			@RequestBody Tweet req,
			@RequestHeader("Authorization") String jwtToken)
			throws UserException, TweetException {

		logger.info("Received request to create a new tweet.");

		User user = userService.findUserProfileByJwt(jwtToken);
		logger.info("User identified: {}", user.getEmail());

		Tweet tweet = tweetService.createTweet(req, user);
		logger.info("Tweet created successfully. tweetId={}", tweet.getId());

		TweetDto tweetDto = TweetDtoMapper.toTweetDto(tweet, user);

		return new ResponseEntity<>(tweetDto, HttpStatus.CREATED);
	}

	/**
	 * POST /reply : Reply to a tweet.
	 *
	 * @param req      reply payload (includes original tweetId)
	 * @param jwtToken Bearer token
	 * @return the reply TweetDto
	 * @throws UserException  if authentication fails
	 * @throws TweetException if reply fails
	 */
	@PostMapping("/reply")
	public ResponseEntity<TweetDto> replyTweet(
			@RequestBody TweetReplyRequest req,
			@RequestHeader("Authorization") String jwtToken)
			throws UserException, TweetException {

		logger.info("Received request to reply to tweetId={}", req.getTweetId());
		User user = userService.findUserProfileByJwt(jwtToken);
		logger.info("User identified: {}", user.getEmail());

		Tweet tweet = tweetService.createdReply(req, user);
		logger.info("Reply posted successfully. replyTweetId={}", tweet.getId());

		TweetDto tweetDto = TweetDtoMapper.toTweetDto(tweet, user);
		return new ResponseEntity<>(tweetDto, HttpStatus.CREATED);
	}

	/**
	 * POST /{tweetId}/retweet : Retweet an existing tweet.
	 *
	 * @param tweetId  ID to retweet
	 * @param jwtToken Bearer token
	 * @return the retweeted TweetDto
	 * @throws UserException  if authentication fails
	 * @throws TweetException if retweet fails
	 */
	@PostMapping("/{tweetId}/retweet")
	public ResponseEntity<TweetDto> reTweet(
			@PathVariable Long tweetId,
			@RequestHeader("Authorization") String jwtToken)
			throws UserException, TweetException {

		User user = userService.findUserProfileByJwt(jwtToken);
		Tweet tweet = tweetService.retweet(tweetId, user);
		TweetDto tweetDto = TweetDtoMapper.toTweetDto(tweet, user);

		return new ResponseEntity<>(tweetDto, HttpStatus.OK);
	}

	/**
	 * GET /{tweetId} : Retrieve a tweet by ID.
	 *
	 * @param tweetId  tweet ID
	 * @param jwtToken Bearer token
	 * @return the TweetDto
	 * @throws UserException  if authentication fails
	 * @throws TweetException if not found
	 */
	@GetMapping("/{tweetId}")
	public ResponseEntity<TweetDto> findByTweetId(
			@PathVariable Long tweetId,
			@RequestHeader("Authorization") String jwtToken)
			throws UserException, TweetException {

		logger.info("Received retweet request for tweetId={}", tweetId);
		User user = userService.findUserProfileByJwt(jwtToken);
		logger.info("User identified: {}", user.getEmail());

		Tweet tweet = tweetService.findbyId(tweetId);
		logger.info("Retweet successful. newTweetId={}", tweet.getId());

		TweetDto tweetDto = TweetDtoMapper.toTweetDto(tweet, user);
		return new ResponseEntity<>(tweetDto, HttpStatus.OK);
	}

	/**
	 * DELETE /{tweetId} : Remove a tweet.
	 *
	 * @param tweetId  tweet ID
	 * @param jwtToken Bearer token
	 * @return status message
	 * @throws UserException  if authentication fails
	 * @throws TweetException if deletion fails
	 */
	@DeleteMapping("/{tweetId}")
	public ResponseEntity<ApiResponse> deleteTweet(
			@PathVariable Long tweetId,
			@RequestHeader("Authorization") String jwtToken)
			throws UserException, TweetException {

		logger.info("Request to delete tweetId={}", tweetId);
		User user = userService.findUserProfileByJwt(jwtToken);
		logger.info("User identified: {}", user.getEmail());

		tweetService.deleteTweetById(tweetId, user.getId());
		logger.info("Tweet deleted successfully. tweetId={}", tweetId);

		// ApiResponse response = new ApiResponse("Tweet has been deleted", true);
		// OR
		ApiResponse response = new ApiResponse();
		response.setMessage("Tweet has been deleted");
		response.setStatus(true);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * GET /user/{userId} : List all tweets by a user.
	 *
	 * @param userId   owner ID
	 * @param jwtToken Bearer token
	 * @return list of TweetDto
	 * @throws UserException  if authentication fails
	 * @throws TweetException on error
	 */
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<TweetDto>> getUsersAllTweets(
			@PathVariable Long userId,
			@RequestHeader("Authorization") String jwtToken)
			throws UserException, TweetException {

		logger.info("Fetching all tweets for userId={}", userId);
		User user = userService.findUserProfileByJwt(jwtToken);
		logger.info("User identified: {}", user.getEmail());

		List<Tweet> tweets = tweetService.getUserTweets(user);
		logger.info("Found {} tweets for userId={}", tweets.size(), userId);

		List<TweetDto> tweetDtos = TweetDtoMapper.toTweetDtos(tweets, user);

		return new ResponseEntity<>(tweetDtos, HttpStatus.OK);
	}

	/**
	 * GET /user/{userId}/likes : List tweets liked by a user.
	 *
	 * @param userId   owner ID
	 * @param jwtToken Bearer token
	 * @return list of TweetDto
	 * @throws UserException  if authentication fails
	 * @throws TweetException on error
	 */
	@GetMapping("/user/{userId}/likes")
	public ResponseEntity<List<TweetDto>> findTweetByLikesContainsUser(
			@PathVariable Long userId,
			@RequestHeader("Authorization") String jwtToken)
			throws UserException, TweetException {

		logger.info("Fetching liked tweets for userId={}", userId);

		User user = userService.findUserProfileByJwt(jwtToken);
		logger.info("User identified: {}", user.getEmail());

		List<Tweet> tweets = tweetService.findByLikesContainsUser(user);
		logger.info("Found {} liked tweets for userId={}", tweets.size(), userId);

		List<TweetDto> tweetDtos = TweetDtoMapper.toTweetDtos(tweets, user);

		return new ResponseEntity<>(tweetDtos, HttpStatus.OK);
	}

}
