package com.barrage.service.impl;

import com.barrage.dao.UserChannelRelationDao;
import com.barrage.service.UserChannelRelationService;

public class UserChannelRelationServiceImpl implements UserChannelRelationService {

	private UserChannelRelationDao userChannelRelationDao;

	public void join(String fromUserName, long channelId) {
		userChannelRelationDao.join(fromUserName, channelId);
	}

	public void quit(String fromUserName) {
		userChannelRelationDao.quit(fromUserName);
	}

	public Long getJoinChannelId(String fromUserName) {
		return userChannelRelationDao.getJoinChannelId(fromUserName);
	}

	public UserChannelRelationDao getUserChannelRelationDao() {
		return userChannelRelationDao;
	}

	public void setUserChannelRelationDao(UserChannelRelationDao userChannelRelationDao) {
		this.userChannelRelationDao = userChannelRelationDao;
	}

}
