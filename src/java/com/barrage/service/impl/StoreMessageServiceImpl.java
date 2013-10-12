package com.barrage.service.impl;

import com.barrage.service.StoreMessageService;

public class StoreMessageServiceImpl implements StoreMessageService {

	private StoreMessageService storeMessageServiceDao;

	@Override
	public void saveMessage(Long userId, Long channelId, String content) {
		storeMessageServiceDao.saveMessage(userId, channelId, content);
	}

	public StoreMessageService getStoreMessageServiceDao() {
		return storeMessageServiceDao;
	}

	public void setStoreMessageServiceDao(StoreMessageService storeMessageServiceDao) {
		this.storeMessageServiceDao = storeMessageServiceDao;
	}

}
