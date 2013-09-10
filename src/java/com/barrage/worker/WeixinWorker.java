package com.barrage.worker;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.barrage.model.Channel;
import com.barrage.service.ChannelManager;
import com.weixin.send.SendMsg;
import com.weixin.send.Sender;
import com.weixin.send.http.WeixinHttpSender;

public class WeixinWorker implements Sender {

	protected Logger log = LogManager.getLogger("weixinServer");

	private BlockingQueue<SendMsg> cache = new ArrayBlockingQueue<SendMsg>(1000);

	private ChannelManager channelManager;

	private WeixinHttpSender weixinHttpSender;

	private WebPageSender webPageSender;

	private SendMsgSaver sendMsgSaver;

	boolean isStop = false;

	/**
	 * 发送消息处理类统一入口
	 */
	public void init() {
		weixinHttpSender.start();
		webPageSender.start();
		sendMsgSaver.start();
		start();
	}

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

								log.info("[WeixinWorker]消息指派: " + msg);

								// 根据频道类型发送指定客户端
								long channelId = msg.getChannelId();
								Channel channel = channelManager.findChannelId(channelId);

								// 输出到WX界面
								if (channel.getOutputType() % 10 == 1) {
									weixinHttpSender.send(msg);
								}

								// 输出到WEBPAGE
								if (channel.getOutputType() / 10 == 1) {
									webPageSender.send(msg);
								}

								// 保存数据
								if (channel.getIsStore().equals(1l)) {
									sendMsgSaver.send(msg);
								}

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

	public ChannelManager getChannelManager() {
		return channelManager;
	}

	public void setChannelManager(ChannelManager channelManager) {
		this.channelManager = channelManager;
	}

	public WeixinHttpSender getWeixinHttpSender() {
		return weixinHttpSender;
	}

	public void setWeixinHttpSender(WeixinHttpSender weixinHttpSender) {
		this.weixinHttpSender = weixinHttpSender;
	}

	public WebPageSender getWebPageSender() {
		return webPageSender;
	}

	public void setWebPageSender(WebPageSender webPageSender) {
		this.webPageSender = webPageSender;
	}

	public SendMsgSaver getSendMsgSaver() {
		return sendMsgSaver;
	}

	public void setSendMsgSaver(SendMsgSaver sendMsgSaver) {
		this.sendMsgSaver = sendMsgSaver;
	}

}
