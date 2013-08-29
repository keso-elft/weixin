package com.barrage.dao.hibernate;

import java.io.Serializable;

import com.barrage.dao.UserChannelRelationDao;
import com.barrage.model.UserChannelRelation;

public class UserChannelRelationHibernate extends EntityDaoHibernate<UserChannelRelation, Serializable> implements
		UserChannelRelationDao {

	@Override
	public boolean join(long userId, long channelId) {
		return false;
	}

	@Override
	public boolean quit(long userId) {
		return false;
	}

}
