package com.barrage.service;

public interface UserChannelRelationService {

	public void join(String fromUserName, long channelId);

	public void quit(String fromUserName);

	public Long getJoinChannelId(String fromUserName);

}
