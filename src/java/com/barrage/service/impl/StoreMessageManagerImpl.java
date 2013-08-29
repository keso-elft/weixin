package com.barrage.service.impl;

import java.io.Serializable;

import com.barrage.dao.StoreMessageDao;
import com.barrage.model.StoreMessage;
import com.barrage.service.StoreMessageManager;

public class StoreMessageManagerImpl extends EntityManagerImpl<StoreMessage, Serializable> implements StoreMessageManager{

	StoreMessageDao storeMessageDao;

	public StoreMessageDao getStoreMessageDao() {
		return storeMessageDao;
	}

	public void setStoreMessageDao(StoreMessageDao storeMessageDao) {
		this.storeMessageDao = storeMessageDao;
	}

}
