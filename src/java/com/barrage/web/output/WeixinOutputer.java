package com.barrage.web.output;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.barrage.web.output.db.SendMsgSaver;
import com.barrage.web.output.servlet.ChannelCometServlet;

public class WeixinOutputer implements Sender {

	protected Logger log = LogManager.getLogger("weixinOutput");

	private BlockingQueue<SendMsg> cache = new ArrayBlockingQueue<SendMsg>(1000);

	private WeixinOutputRunner weixinOutputRunner;

	private SendMsgSaver sendMsgSaver;

	boolean isStop = false;

	public void init() {
		Thread sendMsgSaverThread = new Thread(sendMsgSaver, "sendMsgSaver");
		sendMsgSaverThread.setDaemon(true);
		sendMsgSaverThread.start();

		weixinOutputRunner = new WeixinOutputRunner();
		Thread weixinOutputThread = new Thread(weixinOutputRunner, "weixinOutputThread");
		weixinOutputThread.start();
	}

	public void destroy() {
		sendMsgSaver.stop();
		sendMsgSaver = null;

		weixinOutputRunner.stop();
		weixinOutputRunner = null;

		// TODO 待保存
		cache.clear();
	}

	/**
	 * 发送消息处理类统一入口
	 */
	public void send(SendMsg sendMsg) {
		cache.offer(sendMsg);
	}

	public class WeixinOutputRunner implements Runnable {

		protected boolean running = true;

		public WeixinOutputRunner() {

		}

		public void stop() {
			running = false;
		}

		public void run() {
			while (running) {
				try {
					SendMsg msg = (SendMsg) cache.take();
					if (msg == null)
						continue;

					log.info("[WeixinOutputer]消息指派: " + msg);

					// 根据频道类型发送指定客户端
					long outputType = msg.getOutputType();

					// 输出到WX界面
					if ((outputType & 0x1) != 0) {
					}

					// 输出到WEBPAGE
					if ((outputType & 0x2) != 0) {
						ChannelCometServlet.send(msg);
					}

					// 输出到数据库
					if ((outputType & 0x4) != 0) {
						sendMsgSaver.send(msg);
					}

				} catch (Throwable e) {
					log.error("消息发送失败", e);
				}
			}
		}
	}

	public SendMsgSaver getSendMsgSaver() {
		return sendMsgSaver;
	}

	public void setSendMsgSaver(SendMsgSaver sendMsgSaver) {
		this.sendMsgSaver = sendMsgSaver;
	}

}
