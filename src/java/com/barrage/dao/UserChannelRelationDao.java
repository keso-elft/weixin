package com.barrage.dao;

import java.io.Serializable;

import com.barrage.model.UserChannelRelation;

public interface UserChannelRelationDao extends EntityDao<UserChannelRelation, Serializable> {

	public boolean join(long userId, long channelId);

	public boolean quit(long userId);

}
