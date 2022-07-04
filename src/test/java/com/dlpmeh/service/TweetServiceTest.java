
package com.dlpmeh.service;

import com.dlpmeh.config.model.Tweet;
import com.dlpmeh.config.model.User;
import com.dlpmeh.exception.TweetException;
import com.dlpmeh.request.TweetReplyRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TweetServiceTest {

    @Autowired
    private TweetService tweetService;

    @Autowired
    private UserService userService;

    static Long tweetId;
    static User user;

    @BeforeAll
    public static void setup(@Autowired UserService userService, @Autowired TweetService tweetService)
            throws Exception {
        // Create user
        User newUser = new User();
        newUser.setEmail("tweetuser@test.com");
        newUser.setPassword("tweetpass");
        newUser.setUsername("tweetUser");
        user = userService.registerUser(newUser);

        // Create tweet
        Tweet tweet = new Tweet();
        tweet.setContent("Initial test tweet");
        Tweet savedTweet = tweetService.createTweet(tweet, user);
        tweetId = savedTweet.getId();
    }

    @Test
    @Order(1)
    public void testCreateTweet() throws Exception {
        Tweet tweet = new Tweet();
        tweet.setContent("This is another tweet");
        Tweet saved = tweetService.createTweet(tweet, user);

        assertNotNull(saved.getId());
        assertEquals("This is another tweet", saved.getContent());
    }

    @Test
    @Order(2)
    public void testFindAllTweets() {
        List<Tweet> allTweets = tweetService.findAllTweets();
        assertFalse(allTweets.isEmpty());
    }

    @Test
    @Order(3)
    public void testFindById() throws Exception {
        Tweet found = tweetService.findbyId(tweetId);
        assertEquals("Initial test tweet", found.getContent());
    }

    @Test
    @Order(4)
    public void testGetUserTweets() {
        List<Tweet> userTweets = tweetService.getUserTweets(user);
        assertFalse(userTweets.isEmpty());
    }

    @Test
    @Order(5)
    public void testDeleteTweetById() throws Exception {
        tweetService.deleteTweetById(tweetId, user.getId());
        assertThrows(TweetException.class, () -> tweetService.findbyId(tweetId));
    }

    @Test
    @Order(6)
    public void testReplyTweet() throws Exception {
        // Use default constructor and set content manually
        Tweet tweet = new Tweet();
        tweet.setContent("Parent tweet");
        tweet = tweetService.createTweet(tweet, user); // Save the parent tweet

        // Use correct field name: setTweetld (NOT setParentTweetId)
        TweetReplyRequest replyRequest = new TweetReplyRequest();
        replyRequest.setContent("This is a reply");
        replyRequest.setTweetId(tweet.getId()); // Your class uses tweetld, not parentTweetId

        // Create reply
        Tweet reply = tweetService.createdReply(replyRequest, user);

        // Assert reply exists and content is correct
        assertNotNull(reply.getId());
        assertEquals("This is a reply", reply.getContent());
    }

}
