package com.barrage.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barrage.dao.ChannelDao;
import com.barrage.model.Channel;

public class ChannelManager {

	/**
	 * 所有频道缓存,[channelId,Channel]
	 */
	private Map<Long, Channel> channelMap = new HashMap<Long, Channel>();

	private Object lock = new Object();

	private ChannelDao channelDao;

	private long refreshPeriod = 60 * 60 * 1000;

	public void init() {
		new Thread(new ChannelRefreshThread(), "ChannelRefreshThread").start();
	}

	/**
	 * 数据库保存
	 */
	public Long createChannel(Long userId, String name, String password) {
		Channel channel = new Channel();
		channel.setName(name);
		channel.setPassword(password);
		channel.setCreateUserId(userId);
		channel.setCreateTime(new Date());

		Long id = (Long) channelDao.save(channel);
		reloadChannel(id);

		return id;
	}

	/**
	 * 缓存查channel
	 */
	public Channel findChannelId(Long channelId) {
		if (channelId == null) {
			return null;
		}
		return channelMap.get(channelId);
	}

	/**
	 * 同步重载channel
	 */
	public void reloadChannel(Long channelId) {
		Channel loadInfo = channelDao.get(channelId);
		synchronized (lock) {
			Channel info = (Channel) channelMap.get(channelId);
			if (loadInfo == null) {
				if (info != null) {
					channelMap.remove(channelId);
				}
			} else if (info == null) {
				channelMap.put(channelId, loadInfo);
			} else {
				channelMap.remove(info.getId());
				channelMap.put(loadInfo.getId(), loadInfo);
			}
		}
	}

	/**
	 * 同步重载全部
	 */
	public void reloadAll() {
		List<Channel> newInfoList = channelDao.getAllValidChannel();

		for (Channel newInfo : newInfoList) {
			synchronized (lock) {
				Channel info = (Channel) channelMap.get(newInfo.getId());
				if (info != null) {
					channelMap.remove(info.getId());
				}
				channelMap.put(newInfo.getId(), newInfo);
			}
		}

		Long[] channelIds = null;
		synchronized (lock) {
			channelIds = channelMap.keySet().toArray(new Long[channelMap.size()]);
		}

		for (Long channelId : channelIds) {
			Channel oldInfo = channelMap.get(channelId);
			boolean have = false;
			for (Channel newInfo : newInfoList) {
				if (newInfo.getId().equals(oldInfo.getId())) {
					have = true;
					break;
				}
			}
			if (!have) {
				System.out.println("频道到期或失效，已从缓存去除:" + channelId);
				synchronized (lock) {
					channelMap.remove(channelId);
				}
			}
		}
	}

	/**
	 * 缓存定时同步
	 */
	public class ChannelRefreshThread implements Runnable {
		public void run() {
			reloadAll();
			try {
				Thread.sleep(refreshPeriod);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public ChannelDao getChannelDao() {
		return channelDao;
	}

	public void setChannelDao(ChannelDao channelDao) {
		this.channelDao = channelDao;
	}

	public long getRefreshPeriod() {
		return refreshPeriod;
	}

	public void setRefreshPeriod(long refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}

}
