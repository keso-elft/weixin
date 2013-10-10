package com.barrage.dao;

public interface UserChannelRelationDao {

	public void join(String fromUserName, long channelId);

	public void quit(String fromUserName);
	
	public Long getJoinChannelId(String fromUserName);

}
