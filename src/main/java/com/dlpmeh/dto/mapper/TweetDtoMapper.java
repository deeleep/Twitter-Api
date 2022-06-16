package com.dlpmeh.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.dlpmeh.config.model.Tweet;
import com.dlpmeh.config.model.User;
import com.dlpmeh.dto.TweetDto;
import com.dlpmeh.dto.UserDto;
import com.dlpmeh.util.TweetUtil;

public class TweetDtoMapper {
	
	public static TweetDto toTweetDto(Tweet tweet, User reqUser) {
	    UserDto user = UserDtoMapper.toUserDto(tweet.getUser());
	    boolean isLiked = TweetUtil.isLikedByReqUser(reqUser, tweet);
	    boolean isRetweeted = TweetUtil.isRetwitedByReqUser(reqUser, tweet);
	    
	    List<Long> retweetUserId=new ArrayList<>();
	    for (User retweetUser : tweet.getRetweetUser()) {
	        retweetUserId.add(retweetUser.getId());  // Make sure getId() returns a Long
	    }
	    
	    TweetDto tweetDto = new TweetDto();
	    tweetDto.setId(tweet.getId());
	    tweetDto.setContent(tweet.getContent());
	    tweetDto.setCreatedAt(tweet.getCreatedAt());
	    tweetDto.setImage(tweet.getImage());
	    tweetDto.setTotalLikes(tweet.getLikes().size());
	    tweetDto.setTotalReplies(tweet.getReplyTweets().size());
	    tweetDto.setTotalReTweets(tweet.getRetweetUser().size());
	    tweetDto.setUser(user);
	    tweetDto.setLiked(isLiked);             // Setting whether the tweet is liked by the requested user
	    tweetDto.setReTweet(isRetweeted);       // Setting whether the tweet is retweeted by the requested user
	  
	    tweetDto.setReTweetusersId(retweetUserId);	    
	    tweetDto.setReplyTweets(toTweetDtos(tweet.getReplyTweets(), reqUser));
	    tweetDto.setVideo(tweet.getVideo());
	  
	    return tweetDto;
	    
	    
	}
	
	public static List<TweetDto> toTweetDtos(List<Tweet> tweets, User reqUser) {
	    List<TweetDto> tweetDtos = new ArrayList<>();

	    for (Tweet tweet : tweets) {
	        TweetDto tweetDto = toReplyTweetDto(tweet, reqUser);
	        tweetDtos.add(tweetDto);
	    }

	    return tweetDtos;
	}


	private static TweetDto toReplyTweetDto(Tweet tweet, User reqUser) {
		UserDto user = UserDtoMapper.toUserDto(tweet.getUser());
	    boolean isLiked = TweetUtil.isLikedByReqUser(reqUser, tweet);
	    boolean isRetwited = TweetUtil.isRetwitedByReqUser(reqUser, tweet);
	    
	    List<Long> retweetUserId=new ArrayList<>();
	    for (User user1 : tweet.getRetweetUser()) {
	        retweetUserId.add(user1.getId());  
	    }
	    
	    TweetDto tweetDto = new TweetDto();
	    tweetDto.setId(tweet.getId());
	    tweetDto.setContent(tweet.getContent());
	    tweetDto.setCreatedAt(tweet.getCreatedAt());
	    tweetDto.setImage(tweet.getImage());
	    tweetDto.setTotalLikes(tweet.getLikes().size());
	    tweetDto.setTotalReplies(tweet.getReplyTweets().size());
	    tweetDto.setTotalReTweets(tweet.getRetweetUser().size());
	    tweetDto.setUser(user);
	    tweetDto.setLiked(isLiked);            // Setting whether the tweet is liked by the requested user
	    tweetDto.setReTweet(isRetwited);       // Setting whether the tweet is retweeted by the requested user
	  
	    tweetDto.setReTweetusersId(retweetUserId);	    
	    tweetDto.setVideo(tweet.getVideo());
	  
	    return tweetDto;
	}


}
