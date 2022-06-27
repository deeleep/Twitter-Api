
package com.dlpmeh.service;

import com.dlpmeh.config.model.User;
import com.dlpmeh.config.model.Tweet;
import com.dlpmeh.config.model.Like;
import com.dlpmeh.exception.TweetException;
import com.dlpmeh.exception.UserException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @Autowired
    private TweetService tweetService;

    static Long tweetId;
    static Long userId;

    @BeforeAll
    public static void init(@Autowired UserService userService, @Autowired TweetService tweetService) throws Exception {
        // Create user
        User user = new User();
        user.setEmail("like@test.com");
        user.setPassword("pass123");
        user.setUsername("likeUser");
        user = userService.registerUser(user); // save & get full object
        userId = user.getId();

        // Create tweet using full User object
        Tweet tweet = new Tweet();
        tweet.setContent("This is a tweet to be liked");
        tweetId = tweetService.createTweet(tweet, user).getId(); // pass user, not ID
    }

    @Test
    @Order(1)
    public void testLikeTweet() throws UserException, TweetException {
        User user = userService.findUserById(userId);
        Like like = likeService.likeTweet(tweetId, user);
        assertNotNull(like.getId());
    }

    @Test
    @Order(2)
    public void testGetAllLikes() throws TweetException {
        List<Like> likes = likeService.getAllLikes(tweetId);
        assertTrue(likes.size() > 0);
    }
}
