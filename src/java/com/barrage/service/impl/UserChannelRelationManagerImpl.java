package com.barrage.service.impl;

import java.io.Serializable;
import java.util.List;

import com.barrage.dao.UserChannelRelationDao;
import com.barrage.model.UserChannelRelation;
import com.barrage.service.UserChannelRelationManager;
import com.weixin.common.WeiXinFans;

public class UserChannelRelationManagerImpl extends EntityManagerImpl<UserChannelRelation, Serializable> implements
		UserChannelRelationManager {

	UserChannelRelationDao userChannelRelationDao;

	public boolean join(long userId, long channelId) {
		return userChannelRelationDao.join(userId, channelId);
	}

	public boolean quit(long userId) {
		return userChannelRelationDao.quit(userId);
	}

	@Override
	public Long getJoinChannelId(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WeiXinFans> getAllUser(long channelId) {
		// TODO Auto-generated method stub
		return null;
	}

	public UserChannelRelationDao getUserChannelRelationDao() {
		return userChannelRelationDao;
	}

	public void setUserChannelRelationDao(UserChannelRelationDao userChannelRelationDao) {
		this.userChannelRelationDao = userChannelRelationDao;
	}

}
