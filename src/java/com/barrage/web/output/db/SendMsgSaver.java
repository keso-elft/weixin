package com.barrage.web.output.db;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.barrage.dao.StoreMessageDao;
import com.barrage.web.output.SendMsg;
import com.barrage.web.output.Sender;

/**
 * 消息入库进程
 */
public class SendMsgSaver implements Sender, Runnable {

	protected Logger log = LogManager.getLogger("weixinOutput");

	private BlockingQueue<SendMsg> cache = new ArrayBlockingQueue<SendMsg>(1000);

	StoreMessageDao storeMessageDao;

	boolean isStop = false;

	@Override
	public boolean send(SendMsg sendMsg) {
		return cache.offer(sendMsg);
	}

	@Override
	public void run() {
		if (!isStop) {
			try {
				new Thread() {
					public void run() {
						while (true) {
							try {
								SendMsg msg = (SendMsg) cache.take();
								if (msg == null)
									continue;

								// TODO 单条存储,有点慢
								storeMessageDao.saveMessage(msg.getUserId(), msg.getChannelId(), msg.getContent());

								log.info("[SendMsgSaver]消息入库: " + msg);

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

}
