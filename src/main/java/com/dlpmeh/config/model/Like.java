package com.dlpmeh.config.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a user's like on a tweet, linking the user and the liked tweet.
 */
@Entity
@Data
@Table(name = "likes")
public class Like {
	// Unique identifier for the like
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	// List of users who liked the tweet
	@ManyToOne
	private User user;

	// List of tweets that have been liked
	@ManyToOne
	private Tweet tweet;

}
