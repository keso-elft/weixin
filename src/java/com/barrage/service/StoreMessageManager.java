package com.barrage.service;

import com.barrage.dao.StoreMessageDao;

public class StoreMessageManager {

	StoreMessageDao storeMessageDao;

	public void init() {
	}

	public StoreMessageDao getStoreMessageDao() {
		return storeMessageDao;
	}

	public void setStoreMessageDao(StoreMessageDao storeMessageDao) {
		this.storeMessageDao = storeMessageDao;
	}

}
