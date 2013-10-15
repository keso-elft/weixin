package com.barrage.web.output.http;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.barrage.web.output.SendMsg;
import com.barrage.web.output.Sender;

public class WeixinHttpSender implements Sender {

	protected Logger log = LogManager.getLogger("weixinOutput");

	private BlockingQueue<SendMsg> cache = new ArrayBlockingQueue<SendMsg>(1000);

	boolean isStop = false;

	public boolean send(SendMsg sendMsg) {
		return cache.offer(sendMsg);
	}

	public void start() {
		if (!isStop) {
			try {
				new Thread() {
					public void run() {
						while (true) {
							try {
								SendMsg msg = (SendMsg) cache.take();
								if (msg == null)
									continue;

								if (!HttpSendTools.haseCookie())
									HttpSendTools.auth();

								// long channelId = msg.getChannelId();
								// 群发
								// List<String> list =
								// userChannelRelationManager.getChannelAllUser(channelId);
								// for (String fromUserName : list) {
								// // 自己不发
								// if
								// (!msg.getFromUserName().equals(fromUserName))
								// {
								// User fan =
								// weiXinFansManager.getUser(fromUserName);
								// if (fan.getFakeId() != null) {
								// String rtnMessage = HttpSendTools
								// .sendMsg(msg.getContent(), fan.getFakeId());
								// log.info("消息发送回复:" + rtnMessage);
								// }
								// // TODO fakeId同步
								// }
								// }

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

}
