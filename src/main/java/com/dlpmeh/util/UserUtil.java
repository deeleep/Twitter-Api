package com.dlpmeh.util;

import com.dlpmeh.config.model.User;

public class UserUtil {

	public static final boolean isReqUser(User reqUser, User targetUser) {
		return reqUser.getId().equals(targetUser.getId());
	}

	public static final boolean isFollowedByReqUser(User reqUser, User targetUser) {
		return reqUser.getFollowingUsers().contains(targetUser);
	}

}
