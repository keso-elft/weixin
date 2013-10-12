package com.barrage.dao.impl;

import java.util.Date;

import com.barrage.dao.StoreMessageDao;
import com.barrage.model.StoreMessage;

public class StoreMessageDaoImpl extends SuperDao implements StoreMessageDao {

	@Override
	public void saveMessage(Long userId, Long channelId, String content) {
		StoreMessage msg = new StoreMessage();
		msg.setUserId(userId);
		msg.setChannelId(channelId);
		msg.setContent(content);
		msg.setSendTime(new Date());

		getHibernateTemplate().save(msg);

	}

}
