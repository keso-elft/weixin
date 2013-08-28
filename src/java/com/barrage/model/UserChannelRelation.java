package com.barrage.model;

/**
 * 用户频道关系表
 */
public class UserChannelRelation {

	private Long id;

	private Long userId;

	private Long channelId;

	/**
	 * 状态 0=加入,1=退出
	 */
	private Long status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
}
