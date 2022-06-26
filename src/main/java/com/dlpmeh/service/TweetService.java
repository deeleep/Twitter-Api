package com.dlpmeh.service;

import java.util.List;

import com.dlpmeh.config.model.Tweet;
import com.dlpmeh.config.model.User;
import com.dlpmeh.exception.TweetException;
import com.dlpmeh.exception.UserException;
import com.dlpmeh.request.TweetReplyRequest;

/**
 * Service interface for managing tweets.
 */
public interface TweetService {

    public Tweet createTweet(Tweet request, User user) throws UserException;

    public List<Tweet> findAllTweets();

    public Tweet retweet(Long tweetId, User user) throws UserException, TweetException;

    public Tweet findbyId(Long tweetId) throws TweetException;

    public void deleteTweetById(Long tweetId, Long userId) throws TweetException, UserException;

    public Tweet removeFromRetweet(Long tweetId, User user) throws TweetException, UserException;

    public Tweet createdReply(TweetReplyRequest request, User user) throws TweetException;

    public List<Tweet> getUserTweets(User user);

    public List<Tweet> findByLikesContainsUser(User user);

}
