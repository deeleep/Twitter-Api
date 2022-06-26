package com.dlpmeh.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dlpmeh.config.model.Tweet;
import com.dlpmeh.config.model.User;

/**
 * Repository interface for Tweet entity, with CRUD operations and custom
 * queries for user, retweets, and likes.
 */
public interface TweetRepository extends JpaRepository<Tweet, Long> {

	List<Tweet> findAllByIsTweetTrueOrderByCreatedAtDesc();

	List<Tweet> findByRetweetUserContainsOrUser_IdAndIsTweetTrueOrderByCreatedAtDesc(User user, Long userId);

	List<Tweet> findByLikesContainingOrderByCreatedAtDesc(User user);

	@Query("SELECT t FROM Tweet t JOIN t.likes l WHERE l.user.id = :userId")
	List<Tweet> findByLikesUser_id(Long userId);

}
