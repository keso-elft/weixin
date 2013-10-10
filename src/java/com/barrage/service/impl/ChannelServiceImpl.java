package com.barrage.service.impl;

import java.util.List;

import com.barrage.dao.ChannelDao;
import com.barrage.model.Channel;
import com.barrage.service.ChannelService;

public class ChannelServiceImpl implements ChannelService {

	private ChannelDao channelDao;

	public Long createChannel(Long userId, String name, String password) {
		return channelDao.createChannel(userId, name, password);
	}

	public Channel findChannelById(Long channelId) {
		if (channelId == null) {
			return null;
		}
		return channelDao.findChannelById(channelId);
	}

	public List<Channel> getAllValidChannel() {
		return channelDao.getAllValidChannel();
	}

	public ChannelDao getChannelDao() {
		return channelDao;
	}

	public void setChannelDao(ChannelDao channelDao) {
		this.channelDao = channelDao;
	}

}
