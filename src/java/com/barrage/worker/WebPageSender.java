package com.barrage.worker;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.weixin.send.SendMsg;
import com.weixin.send.Sender;

public class WebPageSender implements Sender {

	protected Logger log = LogManager.getLogger("weixinServer");

	private BlockingQueue<SendMsg> cache = new ArrayBlockingQueue<SendMsg>(1000);

	boolean isStop = false;

	@Override
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

								long channelId = msg.getChannelId();
								// TODO 显示到页面上
								// HttpClient client = new HttpClient();
								log.info("[WebPageSender]消息显示到页面上: " + msg);

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

	public void sendWebMsg(String msg) {
		String url = "ws://localhost:8888/chatsocket";

		// HttpClient http = new HttpClient();
		// PostMethod post = new PostMethod(url.toString());

		// try {
		// post.setDoAuthentication(true);
		//
		// post.setParameter("msg", msg);
		// http.executeMethod(post);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// post.releaseConnection();
		// }
	}

	public void stop() {
		isStop = true;
	}

	public static void main(String[] args) {
		WebPageSender sender = new WebPageSender();
		sender.sendWebMsg("{'body':'asdsad','_xsrf':'dbfd51d2ae3740e3b1909982c6af3363'}");
	}
}
