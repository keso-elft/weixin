package com.weixin.send;

/**
 * 发送消息类
 */
public class SendMsg {

	private String fromUserName;

	private Long channelId;

	private String content;

	public SendMsg(String fromUserName, Long channelId, String content) {
		super();
		this.fromUserName = fromUserName;
		this.channelId = channelId;
		this.content = content;
	}

	public String toString() {
		return "HttpSendMsg[fromUserName=" + fromUserName + ",channelId=" + channelId + ",content=" + content + "]";
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
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

}
