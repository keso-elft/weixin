package com.barrage.service.impl;

import com.barrage.dao.StoreMessageDao;
import com.barrage.service.StoreMessageService;

public class StoreMessageServiceImpl implements StoreMessageService {

	private StoreMessageDao storeMessageDao;

	@Override
	public void saveMessage(Long userId, Long channelId, String content) {
		storeMessageDao.saveMessage(userId, channelId, content);
	}

	public StoreMessageDao getStoreMessageDao() {
		return storeMessageDao;
	}

	public void setStoreMessageDao(StoreMessageDao storeMessageDao) {
		this.storeMessageDao = storeMessageDao;
	}

}
