package com.barrage.dao;

public interface StoreMessageDao {

	public void saveMessage(Long userId, Long channelId, String content);

}
