package com.dlpmeh.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlpmeh.config.model.Tweet;
import com.dlpmeh.config.model.User;
import com.dlpmeh.config.repository.TweetRepository;
import com.dlpmeh.exception.TweetException;
import com.dlpmeh.exception.UserException;
import com.dlpmeh.request.TweetReplyRequest;

@Service
public class TweetServiceImplementation implements TweetService {

	private static final Logger logger = LoggerFactory.getLogger(TweetServiceImplementation.class);

	@Autowired
	private TweetRepository tweetRepository;

	@Override
	public Tweet createTweet(Tweet request, User user) throws UserException {

		logger.info("Creating tweet for userId={}", user.getId());

		Tweet tweet = new Tweet();
		tweet.setContent(request.getContent());
		tweet.setCreatedAt(LocalDateTime.now());
		tweet.setImage(request.getImage());
		tweet.setUser(user);
		tweet.setReply(false);
		tweet.setTweet(true);
		tweet.setVideo(request.getVideo());

		Tweet saved = tweetRepository.save(tweet);
		logger.info("Tweet created successfully: tweetId={}, userId={}", saved.getId(), user.getId());

		return saved;

	}

	@Override
	public List<Tweet> findAllTweets() {
		return tweetRepository.findAllByIsTweetTrueOrderByCreatedAtDesc();

	}

	@Override
	public Tweet retweet(Long tweetId, User user) throws UserException, TweetException {
		logger.info("Processing retweet for tweetId={} by userId={}", tweetId, user.getId());

		Tweet tweet = findbyId(tweetId);
		if (tweet.getRetweetUser().contains(user)) {
			tweet.getRetweetUser().remove(user);
			logger.info("UserId={} unretweeted tweetId={}", user.getId(), tweetId);

		} else {
			tweet.getRetweetUser().add(user);
			logger.info("UserId={} retweeted tweetId={}", user.getId(), tweetId);

		}
		return tweetRepository.save(tweet);
	}

	@Override
	public Tweet findbyId(Long tweetId) throws TweetException {
		logger.debug("Searching for tweetId={}", tweetId);

		Tweet tweet = tweetRepository.findById(tweetId)
				.orElseThrow(() -> {
					logger.warn("Tweet not found with id={}", tweetId);
					return new TweetException("Tweet not found with id " + tweetId);
				});

		logger.info("Tweet found: tweetId={}", tweet.getId());
		return tweet;
	}

	@Override
	public void deleteTweetById(Long tweetId, Long userId) throws TweetException, UserException {
		logger.info("Request to delete tweetId={} by userId={}", tweetId, userId);

		Tweet tweet = findbyId(tweetId);
		if (!userId.equals(tweet.getUser().getId())) {
			logger.warn("Unauthorized delete attempt: userId={} trying to delete tweetId={}", userId, tweetId);
			throw new UserException("you cannot detete another user's tweet");
		}
		tweetRepository.deleteById(tweet.getId());
		logger.info("Tweet deleted successfully: tweetId={}", tweetId);

	}

	@Override
	public Tweet removeFromRetweet(Long tweetId, User user) throws TweetException, UserException {
		// TODO: Add logic later
		logger.info("removeFromRetweet is not yet implemented.");
		return null;
	}

	@Override
	public Tweet createdReply(TweetReplyRequest request, User user) throws TweetException {
		logger.info("Posting reply for tweetId={} by userId={}", request.getTweetId(), user.getId());

		Tweet replForTweet = findbyId(request.getTweetId());
		Tweet tweet = new Tweet();
		tweet.setContent(request.getContent());
		tweet.setCreatedAt(LocalDateTime.now());
		tweet.setImage(request.getImage());
		tweet.setUser(user);
		tweet.setReply(true);
		tweet.setTweet(false);
		tweet.setReplyForTweet(replForTweet);

		Tweet savedReply = tweetRepository.save(tweet);
		logger.info("Reply created: replyId={}, parentTweetId={}", savedReply.getId(), replForTweet.getId());

		tweet.getReplyTweets().add(savedReply);
		tweetRepository.save(replForTweet);
		return replForTweet;

	}

	@Override
	public List<Tweet> getUserTweets(User user) {
		logger.info("Fetching tweets for userId={}", user.getId());
		return tweetRepository.findByRetweetUserContainsOrUser_IdAndIsTweetTrueOrderByCreatedAtDesc(user, user.getId());

	}

	@Override
	public List<Tweet> findByLikesContainsUser(User user) {
		logger.info("Fetching liked tweets for userId={}", user.getId());
		return tweetRepository.findByLikesUser_id(user.getId());
	}

}
