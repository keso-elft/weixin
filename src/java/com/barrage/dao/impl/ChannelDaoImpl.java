package com.barrage.dao.impl;

import java.util.Date;
import java.util.List;

import com.barrage.dao.ChannelDao;
import com.barrage.model.Channel;

public class ChannelDaoImpl extends SuperDao implements ChannelDao {

	public Long createChannel(Long userId, String name, String password) {
		Channel channel = new Channel();
		channel.setName(name);
		channel.setPassword(password);
		// TODO 所有类型
		channel.setOutputType(7l);
		channel.setCreateUserId(userId);
		channel.setCreateTime(new Date());

		Long id = (Long) getHibernateTemplate().save(channel);

		return id;
	}

	@SuppressWarnings("unchecked")
	public Channel findChannelById(Long channelId) {
		List<Channel> result = getHibernateTemplate().find("from Channel where id = ?", channelId);
		if (!result.isEmpty())
			return (Channel) result.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Channel> getAllValidChannel() {
		return getHibernateTemplate().find("from Channel where status = 0");
	}

}
