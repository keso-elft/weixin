package com.barrage.web;

public class AccessToken {

	private String access_token;

	private long expires_in;

	private long getTime;

	public boolean isExpire() {
		return System.currentTimeMillis() - 10 * 60 * 1000 > getTime + expires_in * 1000;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	public long getGetTime() {
		return getTime;
	}

	public void setGetTime(long getTime) {
		this.getTime = getTime;
	}
}
