package com.dlpmeh.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dlpmeh.config.model.Like;
import com.dlpmeh.config.model.Tweet;
import com.dlpmeh.config.model.User;
import com.dlpmeh.config.repository.LikeRepository;
import com.dlpmeh.config.repository.TweetRepository;
import com.dlpmeh.exception.TweetException;
import com.dlpmeh.exception.UserException;

@Service
public class LikeServiceImplementation implements LikeService {

	private static final Logger logger = LoggerFactory.getLogger(LikeServiceImplementation.class);

	@Autowired
	private LikeRepository likeRepository;

	@Autowired
	private TweetService tweetService;

	@Autowired
	private TweetRepository tweetRepository;

	@Override
	public Like likeTweet(Long tweetId, User user) throws UserException, TweetException {

		logger.info("Processing like/unlike for tweetId={} by userId={}", tweetId, user.getId());

		Like isLikeExist = likeRepository.isLikeExist(user.getId(), tweetId);

		if (isLikeExist != null) {
			logger.info("Like already exists. Removing like with id={}", isLikeExist.getId());
			likeRepository.deleteById(isLikeExist.getId());
			return isLikeExist;
		}

		Tweet tweet = tweetService.findbyId(tweetId);

		Like like = new Like();
		like.setTweet(tweet);
		like.setUser(user);

		Like savedLike = likeRepository.save(like);
		logger.info("New like saved: likeId={}, tweetId={}, userId={}", savedLike.getId(), tweetId, user.getId());

		tweet.getLikes().add(savedLike);
		tweetRepository.save(tweet);
		logger.info("Tweet updated with new like: tweetId={}, totalLikes={}", tweet.getId(), tweet.getLikes().size());

		return savedLike;
	}

	@Override
	public List<Like> getAllLikes(Long tweetId) throws TweetException {
		logger.info("Fetching all likes for tweetId={}", tweetId);

		Tweet tweet = tweetService.findbyId(tweetId);
		List<Like> likes = likeRepository.findByTweetId(tweetId);

		logger.info("Found {} likes for tweetId={}", likes.size(), tweetId);

		return likes;
	}

}
