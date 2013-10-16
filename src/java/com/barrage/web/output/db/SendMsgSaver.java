package com.barrage.web.output.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.barrage.dao.StoreMessageDao;
import com.barrage.model.StoreMessage;
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

	private int listSize = 1000;

	@Override
	public void send(SendMsg sendMsg) {
		synchronized (cache) {
			cache.offer(sendMsg);
			cache.notify();
		}
	}

	@Override
	public void run() {
		while (!isStop) {
			try {
				List<SendMsg> list = new ArrayList<SendMsg>(listSize);

				if (cache.drainTo(list, listSize) == 0) {
					try {
						synchronized (cache) {
							cache.wait();
						}
					} catch (InterruptedException e) {
					}
				}

				List<StoreMessage> dbList = SendMsg.toStoreMessages(list);
				storeMessageDao.saveMessages(dbList);

				log.info("[SendMsgSaver]消息入库: " + list.size() + "条");

			} catch (Throwable e) {
				log.error("消息发送失败", e);
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
