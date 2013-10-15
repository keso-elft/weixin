package com.barrage.web.server.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barrage.model.User;
import com.barrage.service.UserService;

/**
 * 用户缓存类
 */
public class UserCacheManager {

	/**
	 * 所有有效用户缓存 [fromUserName,User]
	 */
	private Map<String, User> userMap = new HashMap<String, User>();

	private Object lock = new Object();

	private UserService userService;

	private long sessionRefreshPeriod = 60 * 60 * 1000;

	private UserCacheRefresher userCacheRefresher;

	public void init() {
		userCacheRefresher = new UserCacheRefresher();
		Thread userCacheRefreshThread = new Thread(userCacheRefresher, "UserCacheRefreshThread");
		userCacheRefreshThread.setDaemon(true);
		userCacheRefreshThread.start();
	}

	public void destroy() {
		userMap.clear();
		userCacheRefresher.stop();
		userCacheRefresher = null;
	}

	/**
	 * 用户获取
	 */
	public User getUser(String fromUserName) {
		return userMap.get(fromUserName);
	}

	/**
	 * 用户接入
	 */
	public User accessUser(String fromUserName) {
		User user = userMap.get(fromUserName);
		if (user == null) {
			userService.createUser(fromUserName);
			reloadUser(fromUserName);
		}
		return userMap.get(fromUserName);
	}

	/**
	 * 所有用户
	 */
	public List<User> getUsers() {
		List<User> list = new ArrayList<User>();
		list.addAll(userMap.values());
		return list;
	}

	/**
	 * 从数据库同步重载用户
	 */
	public void reloadUser(String fromUserName) {
		// 数据库获取
		User loadInfo = userService.getUserByFromUserName(fromUserName);

		synchronized (lock) {
			User info = (User) userMap.get(fromUserName);
			if (loadInfo == null) {
				if (info != null) {
					userMap.remove(fromUserName);
				}
			} else if (info == null) {
				userMap.put(fromUserName, loadInfo);
			} else {
				userMap.remove(fromUserName);
				userMap.put(fromUserName, loadInfo);
			}
		}
	}

	/**
	 * 同步重载全部
	 */
	public void reloadAll() {
		List<User> newInfoList = userService.getAllValidUser();
		if (newInfoList == null)
			return;

		for (User newInfo : newInfoList) {
			synchronized (lock) {
				User info = (User) userMap.get(newInfo.getFromUserName());
				if (info != null) {
					userMap.remove(info.getFromUserName());
				}
				userMap.put(newInfo.getFromUserName(), newInfo);
			}
		}
		String[] fromUserNames = null;
		synchronized (lock) {
			fromUserNames = userMap.keySet().toArray(new String[userMap.size()]);
		}
		for (String fromUserName : fromUserNames) {
			User oldInfo = userMap.get(fromUserName);
			boolean have = false;
			for (User newInfo : newInfoList) {
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
	public class UserCacheRefresher implements Runnable {

		protected boolean running = true;

		public void run() {
			while (running) {
				reloadAll();

				try {
					Thread.sleep(sessionRefreshPeriod);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void stop() {
			running = false;
		}

	}

	public long getSessionRefreshPeriod() {
		return sessionRefreshPeriod;
	}

	public void setSessionRefreshPeriod(long sessionRefreshPeriod) {
		this.sessionRefreshPeriod = sessionRefreshPeriod;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserService getUserService() {
		return userService;
	}

}
