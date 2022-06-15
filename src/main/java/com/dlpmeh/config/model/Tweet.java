package com.dlpmeh.config.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

/**
 * Represents a tweet with content, media, and relationships to users, likes,
 * replies, and retweets.
 */

@Entity
@Data
public class Tweet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private User user;

	private String content;
	private String image;
	private String video;

	// A Tweet may have many likes
	@OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
	private List<Like> likes = new ArrayList<>();

	// A Tweet may have many replies
	@OneToMany(mappedBy = "replyForTweet", cascade = CascadeType.ALL)
	private List<Tweet> replyTweets = new ArrayList<>();

	// A Tweet may have many retweets
	@ManyToMany
	private List<User> retweetUser = new ArrayList<>();

	// A Tweet may be a reply to another Tweet
	@ManyToOne
	private Tweet replyForTweet;

	private boolean isReply;
	private boolean isTweet;

	private LocalDateTime createdAt;

}
