package com.barrage.dao;

import java.io.Serializable;
import java.util.List;

import com.barrage.model.Channel;

public interface ChannelDao extends EntityDao<Channel, Serializable> {

	public List<Channel> getAllValidChannel();

}
