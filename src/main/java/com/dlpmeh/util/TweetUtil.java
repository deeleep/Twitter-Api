package com.dlpmeh.util;

import com.dlpmeh.config.model.Like;
import com.dlpmeh.config.model.Tweet;
import com.dlpmeh.config.model.User;

public class TweetUtil {
    // Check if the requested user liked the tweet
    public static boolean isLikedByReqUser(User reqUser, Tweet tweet) {
        for (Like like : tweet.getLikes()) {
            if (like.getUser().getId().equals(reqUser.getId())) {
                return true;
            }
        }
        return false;
    }

    // Check if the requested user retweeted the tweet
    public static boolean isRetwitedByReqUser(User reqUser, Tweet tweet) {
        for (User user : tweet.getRetweetUser()) {
            if (user.getId().equals(reqUser.getId())) {
                return true;
            }
        }
        return false;
    }

}
