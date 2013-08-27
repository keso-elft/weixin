package com.weixin.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 用户缓存类
 */
public class WeiXinFansManager {

	/**
	 * 所有用户缓存,[fromUserName,WeiXinFans]
	 */
	Map<String, WeiXinFans> userMap = new HashMap<String, WeiXinFans>();

	/**
	 * 定期更新时间,默认为一小时
	 */
	static final long sessionRefreshPeriod = 3600 * 1000;

	public WeiXinFansManager() {
		new Thread(new WeiXinFansRefreshThread(), "WeiXinFansRefreshThread").start();
	}

	public synchronized WeiXinFans getUser(String fromUserName) {
		return userMap.get(fromUserName);
	}

	public synchronized WeiXinFans accessUser(String fromUserName) {
		WeiXinFans user = userMap.get(fromUserName);
		if (user != null) {
			user.access();
			userMap.put(fromUserName, user);
		} else {
			user = new WeiXinFans();
			user.setFromUserName(fromUserName);
			user.access();
			userMap.put(fromUserName, user);
		}
		return user;
	}

	/**
	 * 用户缓存定时刷新线程,同数据库/页面同步
	 */
	public class WeiXinFansRefreshThread implements Runnable {

		public void run() {
			Set<String> keys = userMap.keySet();
			for (String key : keys) {
				WeiXinFans user = userMap.get(key);
				if (System.currentTimeMillis() > user.getLastAccessedTime().getTime() + sessionRefreshPeriod) {
					userMap.remove(key);
				}
			}

			try {
				Thread.sleep(sessionRefreshPeriod);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
