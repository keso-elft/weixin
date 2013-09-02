package com.weixin.common;

import java.util.Date;

/**
 *	用户
 */
public class WeiXinFans {

	private long id;

	/**
	 * 接口接入时的代码
	 */
	private String fromUserName;

	/**
	 * 上次接入时间
	 */
	private Date lastAccessedTime;

	/**
	 * HTTP接入时的代码
	 */
	private String fakeId;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 
	 */
	private String remarkName;

	/**
	 * 用户分组
	 */
	private long groupId;

	public void access() {
		this.lastAccessedTime = new Date();
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public Date getLastAccessedTime() {
		return lastAccessedTime;
	}

	public void setLastAccessedTime(Date lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	public String getFakeId() {
		return fakeId;
	}

	public void setFakeId(String fakeId) {
		this.fakeId = fakeId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "WeiXinFans [fakeId=" + fakeId + ", nickName=" + nickName + ", remarkName=" + remarkName + ", groupId="
				+ groupId + "]";
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
}
