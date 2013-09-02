package com.weixin.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barrage.dao.UserDao;

/**
 * 用户缓存类
 */
public class WeiXinFansManager {

	/**
	 * 所有有效用户缓存,[fromUserName,WeiXinFans]
	 */
	private Map<String, WeiXinFans> userMap = new HashMap<String, WeiXinFans>();

	private Object lock = new Object();

	private UserDao userDao;

	private long sessionRefreshPeriod = 60 * 60 * 1000;

	public void init() {
		new Thread(new WeiXinFansRefreshThread(), "WeiXinFansRefreshThread").start();
	}

	/**
	 * 用户获取
	 */
	public WeiXinFans getUser(String fromUserName) {
		return userMap.get(fromUserName);
	}

	/**
	 * 用户接入
	 */
	public WeiXinFans accessUser(String fromUserName) {
		synchronized (lock) {
			WeiXinFans user = userMap.get(fromUserName);
			if (user != null) {
				user.access();
				userMap.put(fromUserName, user);
				userDao.saveOrUpdate(user);
			} else {
				userDao.createUser(fromUserName);
				reloadUser(fromUserName);
			}
			return user;
		}
	}

	/**
	 * 所有用户
	 */
	public List<WeiXinFans> getUsers() {
		List<WeiXinFans> list = new ArrayList<WeiXinFans>();
		list.addAll(userMap.values());
		return list;
	}

	/**
	 * 同步重载用户
	 */
	public void reloadUser(String fromUserName) {
		// 数据库获取
		WeiXinFans loadInfo = userDao.getUserByFromUserName(fromUserName);
		if (loadInfo != null && loadInfo.getFakeId() == null) {
			// TODO HTTP获取并更新
		}

		synchronized (lock) {
			WeiXinFans info = (WeiXinFans) userMap.get(fromUserName);
			if (loadInfo == null) {
				if (info != null) {
					userMap.remove(fromUserName);
				}
			} else if (info == null) {
				userMap.put(fromUserName, loadInfo);
			} else {
				userMap.remove(info.getId());
				userMap.put(loadInfo.getFromUserName(), loadInfo);
			}
		}
	}

	/**
	 * 同步重载全部
	 */
	public void reloadAll() {
		List<WeiXinFans> newInfoList = userDao.getAllValidUser();
		// TODO HTTP获取并更新

		for (WeiXinFans newInfo : newInfoList) {
			synchronized (lock) {
				WeiXinFans info = (WeiXinFans) userMap.get(newInfo.getFromUserName());
				if (info != null) {
					userMap.remove(info.getId());
				}
				userMap.put(newInfo.getFromUserName(), newInfo);
			}
		}
		String[] fromUserNames = null;
		synchronized (lock) {
			fromUserNames = userMap.keySet().toArray(new String[userMap.size()]);
		}
		for (String fromUserName : fromUserNames) {
			WeiXinFans oldInfo = userMap.get(fromUserName);
			boolean have = false;
			for (WeiXinFans newInfo : newInfoList) {
				if (newInfo.getFromUserName().equals(oldInfo.getFromUserName())) {
					have = true;
					break;
				}
			}
			if (!have) {
				System.out.println("用户失效，已从缓存去除:" + fromUserName);
				synchronized (lock) {
					userMap.remove(fromUserName);
				}
			}
		}
	}

	/**
	 * 用户缓存定时刷新线程,同数据库/页面同步
	 */
	public class WeiXinFansRefreshThread implements Runnable {

		public void run() {
			reloadAll();

			try {
				Thread.sleep(sessionRefreshPeriod);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public long getSessionRefreshPeriod() {
		return sessionRefreshPeriod;
	}

	public void setSessionRefreshPeriod(long sessionRefreshPeriod) {
		this.sessionRefreshPeriod = sessionRefreshPeriod;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
