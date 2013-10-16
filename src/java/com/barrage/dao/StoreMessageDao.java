package com.barrage.dao;

import java.util.List;

import com.barrage.model.StoreMessage;

public interface StoreMessageDao {

	public void saveMessage(Long userId, Long channelId, String content);

	public void saveMessages(final List<StoreMessage> list);

}
