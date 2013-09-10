package com.barrage.worker;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.barrage.dao.StoreMessageDao;
import com.barrage.model.StoreMessage;
import com.weixin.common.WeiXinFansManager;
import com.weixin.send.SendMsg;
import com.weixin.send.Sender;

/**
 * 消息入库进程
 */
public class SendMsgSaver implements Sender {

	protected Logger log = LogManager.getLogger("weixinServer");

	private BlockingQueue<SendMsg> cache = new ArrayBlockingQueue<SendMsg>(1000);

	StoreMessageDao storeMessageDao;

	WeiXinFansManager weiXinFansManager;

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

								StoreMessage message = new StoreMessage();
								message.setChannelId(msg.getChannelId());
								message.setContent(msg.getContent());
								message.setUserId(weiXinFansManager.getUser(msg.getFromUserName()).getId());
								message.setSendTime(new Date());

								storeMessageDao.save(message);

								log.info("[SendMsgSaver]消息入库: " + message);

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

	public StoreMessageDao getStoreMessageDao() {
		return storeMessageDao;
	}

	public void setStoreMessageDao(StoreMessageDao storeMessageDao) {
		this.storeMessageDao = storeMessageDao;
	}

	public WeiXinFansManager getWeiXinFansManager() {
		return weiXinFansManager;
	}

	public void setWeiXinFansManager(WeiXinFansManager weiXinFansManager) {
		this.weiXinFansManager = weiXinFansManager;
	}

}
