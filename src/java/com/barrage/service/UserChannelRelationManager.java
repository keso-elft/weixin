package com.barrage.service;

import java.io.Serializable;
import java.util.List;

import com.barrage.model.UserChannelRelation;
import com.weixin.common.WeiXinFans;

public interface UserChannelRelationManager extends EntityManager<UserChannelRelation, Serializable> {

	public boolean join(long userId, long channelId);

	public boolean quit(long userId);

	public Long getJoinChannelId(long userId);

	public List<WeiXinFans> getAllUser(long channelId);

}
