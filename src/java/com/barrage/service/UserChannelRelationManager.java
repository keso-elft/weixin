package com.barrage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barrage.dao.UserChannelRelationDao;
import com.barrage.model.UserChannelRelation;

public class UserChannelRelationManager {

	/**
	 * 所有用户频道现存关系缓存,用户->频道[fromUserName,channelId]
	 */
	private Map<String, Long> userRelationMap = new HashMap<String, Long>();

	/**
	 * 所有用户频道现存关系缓存,频道->用户[channelId,List(fromUserName)]
	 */
	private Map<Long, List<String>> channelRelationMap = new HashMap<Long, List<String>>();

	private Object lock = new Object();

	private UserChannelRelationDao userChannelRelationDao;

	private long refreshPeriod = 60 * 60 * 1000;

	public void init() {
		new Thread(new UserChannelRelationRefreshThread(), "UserChannelRelationRefreshThread").start();
	}

	/**
	 * 加入频道(同步)
	 */
	public void join(String fromUserName, long channelId) {
		synchronized (lock) {
			userChannelRelationDao.join(fromUserName, channelId);
			userRelationMap.put(fromUserName, channelId);
			List<String> list = channelRelationMap.get(channelId);
			if (list == null)
				list = new ArrayList<String>();
			list.add(fromUserName);
			channelRelationMap.put(channelId, list);
		}
	}

	/**
	 * 退出当前频道(同步)
	 */
	public void quit(String fromUserName) {
		synchronized (lock) {
			userChannelRelationDao.quit(fromUserName);
			Long channelId = userRelationMap.remove(fromUserName);
			if (channelId != null) {
				List<String> list = channelRelationMap.get(channelId);
				if (list != null)
					list.remove(fromUserName);
				channelRelationMap.put(channelId, list);
			}
		}
	}

	/**
	 * 频道失效(同步)
	 */
	public void channelInvalid(Long channelId) {
		synchronized (lock) {
			// TODO 无数据库操作
			List<String> userList = channelRelationMap.remove(channelId);
			for (String fromUserName : userList) {
				userRelationMap.remove(fromUserName);
			}
		}
	}

	public Long getJoinChannelId(String fromUserName) {
		return userRelationMap.get(fromUserName);
	}

	public List<String> getChannelAllUser(long channelId) {
		return channelRelationMap.get(channelId);
	}

	/**
	 * 同步重载用户关系
	 */
	public void reloadUserRelation(String fromUserName) {
		UserChannelRelation loadInfo = userChannelRelationDao.getRelationsByFromUserName(fromUserName);
		Long info = userRelationMap.get(fromUserName);
		if (loadInfo == null) {
			if (info != null) {
				quit(fromUserName);
			}
		} else {
			join(fromUserName, loadInfo.getChannelId());
		}
	}

	/**
	 * 同步重载频道关系
	 */
	public void reloadChannelRelation(Long channelId) {
		List<UserChannelRelation> loadInfoList = userChannelRelationDao.getRelationsByFromChannelId(channelId);
		if (loadInfoList == null || loadInfoList.size() == 0) {
			channelInvalid(channelId);
		} else {
			for (UserChannelRelation relation : loadInfoList) {
				String fromUserName = relation.getFromUserName();
				Long cacheChannelId = userRelationMap.get(fromUserName);
				if (!channelId.equals(cacheChannelId)) {
					join(fromUserName, channelId);
				}
			}
		}
	}

	/**
	 * 同步重载全部
	 */
	public void reloadAll() {
		List<UserChannelRelation> newInfoList = userChannelRelationDao.getAllValidRelation();

		for (UserChannelRelation newInfo : newInfoList) {
			join(newInfo.getFromUserName(), newInfo.getChannelId());
		}

		String[] fromUserNames = null;
		synchronized (lock) {
			fromUserNames = userRelationMap.keySet().toArray(new String[userRelationMap.size()]);
		}

		for (String fromUserName : fromUserNames) {
			boolean have = false;
			for (UserChannelRelation newInfo : newInfoList) {
				if (newInfo.getFromUserName().equals(fromUserName)) {
					have = true;
					break;
				}
			}
			if (!have) {
				System.out.println("用户无效，已从用户频道关系缓存去除:" + fromUserName);
				quit(fromUserName);
			}
		}

		Long[] channelIds = null;
		synchronized (lock) {
			channelIds = channelRelationMap.keySet().toArray(new Long[channelRelationMap.size()]);
		}
		for (Long channelId : channelIds) {
			boolean have = false;
			for (UserChannelRelation newInfo : newInfoList) {
				if (newInfo.getChannelId().equals(channelId)) {
					have = true;
					break;
				}
			}
			if (!have) {
				System.out.println("频道无效，已从用户频道关系缓存去除:" + channelId);
				channelInvalid(channelId);
			}
		}
	}

	/**
	 * 缓存定时同步
	 */
	public class UserChannelRelationRefreshThread implements Runnable {
		public void run() {
			reloadAll();
			try {
				Thread.sleep(refreshPeriod);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public UserChannelRelationDao getUserChannelRelationDao() {
		return userChannelRelationDao;
	}

	public void setUserChannelRelationDao(UserChannelRelationDao userChannelRelationDao) {
		this.userChannelRelationDao = userChannelRelationDao;
	}

	public long getRefreshPeriod() {
		return refreshPeriod;
	}

	public void setRefreshPeriod(long refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}

}
