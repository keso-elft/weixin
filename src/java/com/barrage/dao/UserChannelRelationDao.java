package com.barrage.dao;

import java.io.Serializable;
import java.util.List;

import com.barrage.model.UserChannelRelation;

public interface UserChannelRelationDao extends EntityDao<UserChannelRelation, Serializable> {

	public void join(String fromUserName, long channelId);

	public void quit(String fromUserName);

	public UserChannelRelation getRelationsByFromUserName(String fromUserName);

	public List<UserChannelRelation> getRelationsByFromChannelId(Long channelId);

	public List<UserChannelRelation> getAllValidRelation();

}
