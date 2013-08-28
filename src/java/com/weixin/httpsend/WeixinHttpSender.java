package com.weixin.httpsend;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.weixin.common.WeiXinFans;

public class WeixinHttpSender {

	protected Logger log = LogManager.getLogger("weixinServer");

	private BlockingQueue<String> cache = new ArrayBlockingQueue<String>(1000);

	boolean isStop = false;

	public boolean send(String content) {
		return cache.offer(content);
	}

	public void start() {
		if (!isStop) {
			try {
				new Thread() {
					public void run() {
						while (true) {
							try {
								String content = (String) cache.take();
								if (content == null)
									continue;

								Map<String, String> cookie = HttpSendTools.auth("keso.elft@gmail.com", "");

								// 获取粉丝列表
								List<WeiXinFans> list = HttpSendTools.getFans(cookie);

								// 群发
								for (WeiXinFans fans : list) {
									HttpSendTools.sendMsg(cookie, content, fans.getFakeId());
								}

								// 保存到数据库
								log.info("消息发送成功:" + content);
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
