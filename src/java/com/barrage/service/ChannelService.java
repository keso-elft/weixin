package com.barrage.service;

import java.util.List;

import com.barrage.model.Channel;

public interface ChannelService {

	public Long createChannel(Long userId, String name, String password);

	public Channel findChannelById(Long channelId);

	public List<Channel> getAllValidChannel();

}
