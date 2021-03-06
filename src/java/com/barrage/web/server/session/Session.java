package com.barrage.web.server.session;

import java.util.HashMap;
import java.util.Map;

import com.barrage.model.User;

public class Session {

	/**
	 * 用户
	 */
	private User user;

	/**
	 * 上次会话时间
	 */
	private long lastAccessedTime;

	/**
	 * Session缓存记录,如:key:serviceName,stepNo,choiceList,matchId
	 */
	private Map<String, Object> record = new HashMap<String, Object>(); //

	public Session(User user) {
		this.setUser(user);
		lastAccessedTime = System.currentTimeMillis();
	}

	public void access() {
		setLastAccessedTime(System.currentTimeMillis());
	}

	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	public void putRecord(String key, Object value) {
		record.put(key, value);
	}

	public void setServiceName(String serviceName) {
		record.put("serviceName", serviceName);
	}

	public String getServiceName() {
		return (String) record.get("serviceName");
	}

	public void setStepNo(String stepNo) {
		record.put("stepNo", stepNo);
	}

	public String getStepNo() {
		return (String) record.get("stepNo");
	}

	public Object getValue(String key) {
		return record.remove(key);
	}

	public void removeRecord(String key) {
		record.remove(key);
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
