package com.weixin.httpsend;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.barrage.service.UserChannelRelationManager;
import com.weixin.common.WeiXinFans;
import com.weixin.common.WeiXinFansManager;

public class WeixinHttpSender {

	protected Logger log = LogManager.getLogger("weixinServer");

	private BlockingQueue<HttpSendMsg> cache = new ArrayBlockingQueue<HttpSendMsg>(1000);

	private UserChannelRelationManager userChannelRelationManager;

	private WeiXinFansManager weiXinFansManager;

	boolean isStop = false;

	public boolean send(String fromUserName, Long channelId, String content) {
		return cache.offer(new HttpSendMsg(fromUserName, channelId, content));
	}

	public void start() {
		if (!isStop) {
			try {
				new Thread() {
					public void run() {
						while (true) {
							try {
								HttpSendMsg msg = (HttpSendMsg) cache.take();
								if (msg == null)
									continue;

								if (!HttpSendTools.haseCookie())
									HttpSendTools.auth();

								// 获取频道
								long channelId = msg.getChannelId();
								List<String> list = userChannelRelationManager.getChannelAllUser(channelId);

								// 群发
								for (String fromUserName : list) {
									if (!msg.getFromUserName().equals(fromUserName)) {
										WeiXinFans fan = weiXinFansManager.getUser(fromUserName);
										String rtnMessage = HttpSendTools.sendMsg(msg.getContent(), fan.getFakeId());
										log.info("消息发送回复:" + rtnMessage);
									}
								}

								log.info("消息发送完成" + msg);
							} catch (Throwable e) {
								log.error("消息发送失败", e);
							}
						}
					}
				}.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		isStop = true;
	}

	public UserChannelRelationManager getUserChannelRelationManager() {
		return userChannelRelationManager;
	}

	public void setUserChannelRelationManager(UserChannelRelationManager userChannelRelationManager) {
		this.userChannelRelationManager = userChannelRelationManager;
	}

	public WeiXinFansManager getWeiXinFansManager() {
		return weiXinFansManager;
	}

	public void setWeiXinFansManager(WeiXinFansManager weiXinFansManager) {
		this.weiXinFansManager = weiXinFansManager;
	}

}
