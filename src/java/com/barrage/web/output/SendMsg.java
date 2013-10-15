package com.barrage.web.output;

public class SendMsg {

	private Long userId;

	private Long channelId;

	private String content;

	private Long outputType;

	public SendMsg(Long userId, Long channelId, String content, Long outputType) {
		super();
		this.userId = userId;
		this.channelId = channelId;
		this.content = content;
		this.outputType = outputType;
	}

	public String toString() {
		return "[SendMsg]userId=" + userId + ",channelId=" + channelId + ",content=" + content + ",outputType="
				+ outputType;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setOutputType(Long outputType) {
		this.outputType = outputType;
	}

	public Long getOutputType() {
		return outputType;
	}
}
