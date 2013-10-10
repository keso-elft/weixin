package com.weixin.server.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.weixin.common.User;

/**
 * 会话管理器
 */
public class SessionManager {

	/**
	 * 所有Session缓存,[fromUserName,Session],不考虑用户多开情况
	 */
	Map<String, Session> sessionMap = new HashMap<String, Session>();

	/**
	 * Session过期时间,默认一小时
	 */
	static final long sessionTimeout = 3600 * 1000;

	public SessionManager() {
		new Thread(new SessionRefreshThread(), "WeixinSessionRefreshThread").start();
	}

	public synchronized void saveSession(Session session) {
		sessionMap.put(session.getUser().getFromUserName(), session);
	}

	public synchronized Session getSession(String fromUserName) {
		return sessionMap.get(fromUserName);
	}

	/**
	 * 会话连入,若无该用户,需创建user后连入
	 */
	public synchronized void accessSession(User user) {
		Session session = sessionMap.get(user.getFromUserName());
		if (session != null) {
			session.access();
			session.setUser(user);
		} else {
			saveSession(new Session(user));
		}
	}

	public synchronized void removeSession(String fromUserName) {
		sessionMap.remove(fromUserName);
	}

	/**
	 * Session定时清理线程
	 */
	public class SessionRefreshThread implements Runnable {

		public void run() {
			Set<String> keys = sessionMap.keySet();
			for (String key : keys) {
				Session session = sessionMap.get(key);
				if (System.currentTimeMillis() > session.getLastAccessedTime() + sessionTimeout) {
					sessionMap.remove(key);
				}
			}

			try {
				Thread.sleep(sessionTimeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
