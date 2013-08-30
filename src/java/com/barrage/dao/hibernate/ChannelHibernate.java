package com.barrage.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import com.barrage.dao.ChannelDao;
import com.barrage.model.Channel;

public class ChannelHibernate extends EntityDaoHibernate<Channel, Serializable> implements ChannelDao {

	@SuppressWarnings("unchecked")
	public List<Channel> getAllValidChannel() {
		return getHibernateTemplate().find("from Channel where status = ?", 0);
	}

}
