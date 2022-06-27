package com.dlpmeh.service;

import java.util.List;
import com.dlpmeh.config.model.Like;
import com.dlpmeh.config.model.User;
import com.dlpmeh.exception.TweetException;
import com.dlpmeh.exception.UserException;

/**
 * Service interface for managing likes on tweets.
 */
public interface LikeService {

	public Like likeTweet(Long tweetId, User user) throws UserException, TweetException;

	public List<Like> getAllLikes(Long tweetId) throws TweetException;

}
