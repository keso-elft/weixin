package com.barrage.service;

public interface StoreMessageService {

	public void saveMessage(Long userId, Long channelId, String content);

}
