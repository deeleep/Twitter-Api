package com.dlpmeh.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class TweetDto {

    private Long id;
    private String content;
    private String image;
    private String video;
    private UserDto user; // UserDto class
    private LocalDateTime createdAt;

    private int totalLikes;
    private int totalReplies;
    private int totalReTweets;
    private boolean isLiked;
    private boolean isReTweet;

    private List<Long> reTweetusersId;
    private List<TweetDto> replyTweets;

}
