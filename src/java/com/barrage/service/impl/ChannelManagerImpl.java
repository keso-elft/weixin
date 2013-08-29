package com.barrage.service.impl;

import java.io.Serializable;

import com.barrage.dao.ChannelDao;
import com.barrage.model.Channel;
import com.barrage.service.ChannelManager;

public class ChannelManagerImpl extends EntityManagerImpl<Channel, Serializable> implements ChannelManager{

	ChannelDao channelDao;

	public ChannelDao getChannelDao() {
		return channelDao;
	}

	public void setChannelDao(ChannelDao channelDao) {
		this.channelDao = channelDao;
	}
}
