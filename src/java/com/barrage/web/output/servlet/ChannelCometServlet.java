package com.barrage.web.output.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.CometEvent;
import org.apache.catalina.CometProcessor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.barrage.web.output.SendMsg;

public class ChannelCometServlet extends HttpServlet implements CometProcessor {

	protected static Logger log = LogManager.getLogger("weixinOutput");

	private static final long serialVersionUID = -3667180332947986301L;

	// <频道号,List<长连接>>
	protected static Map<Long, List<HttpServletResponse>> connections = new HashMap<Long, List<HttpServletResponse>>();

	// 消息推送线程
	protected static ChannelCometSender channelSender = null;

	public void init() throws ServletException {
		// 启动消息推送线程
		channelSender = new ChannelCometSender();
		Thread channelSenderThread = new Thread(channelSender, "ChannelCometSender["
				+ getServletContext().getContextPath() + "]");
		channelSenderThread.setDaemon(true);
		channelSenderThread.start();
	}

	public void destroy() {
		connections.clear();
		channelSender.stop();
		channelSender = null;
	}

	public void event(CometEvent event) throws IOException, ServletException {
		HttpServletRequest request = event.getHttpServletRequest();
		HttpServletResponse response = event.getHttpServletResponse();

		// 频道号
		Long channelId = null;
		try {
			channelId = Long.parseLong(request.getParameter("channelId"));
		} catch (Exception e) {
		}
		if (channelId == null) {
			return;
		}

		if (event.getEventType() == CometEvent.EventType.BEGIN) {

			log("Begin for session: " + request.getSession(true).getId());
			event.setTimeout(Integer.MAX_VALUE);

			join(channelId, response);

		} else if (event.getEventType() == CometEvent.EventType.ERROR) {

			log("Error for session: " + request.getSession(true).getId());

			quit(channelId, response);
			event.close();

		} else if (event.getEventType() == CometEvent.EventType.END) {

			log("End for session: " + request.getSession(true).getId());

			quit(channelId, response);
			PrintWriter writer = response.getWriter();
			writer.println("</body></html>");
			event.close();

		} else if (event.getEventType() == CometEvent.EventType.READ) {

			InputStream is = request.getInputStream();
			byte[] buf = new byte[512];
			do {
				int n = is.read(buf); // can throw an IOException
				if (n > 0) {
					log("Read " + n + " bytes: " + new String(buf, 0, n) + " for session: "
							+ request.getSession(true).getId());
				} else if (n < 0) {
					return;
				}
			} while (is.available() > 0);
		}
	}

	private void join(Long channelId, HttpServletResponse response) {

		List<HttpServletResponse> temp = connections.get(channelId);
		if (temp == null) {
			temp = new ArrayList<HttpServletResponse>();
		}
		temp.add(response);
		synchronized (connections) {
			connections.put(channelId, temp);
		}
	}

	private void quit(Long channelId, HttpServletResponse response) {

		List<HttpServletResponse> temp = connections.get(channelId);
		if (temp == null) {
			return;
		}
		temp.remove(response);

		if (temp.isEmpty()) {
			synchronized (connections) {
				connections.remove(channelId);
			}
		} else {
			synchronized (connections) {
				connections.put(channelId, temp);
			}
		}
	}

	/**
	 * 向某频道发送消息
	 */
	public static void send(SendMsg message) {
		channelSender.send(message);
	}

	public class ChannelCometSender implements Runnable {

		protected boolean running = true;

		protected BlockingQueue<SendMsg> messages = new ArrayBlockingQueue<SendMsg>(1000);

		public ChannelCometSender() {

		}

		public void stop() {
			running = false;
		}

		// 发送消息
		public void send(SendMsg message) {
			synchronized (messages) {
				messages.offer(message);
				messages.notify();
			}
		}

		public void run() {
			while (running) {
				SendMsg msg = null;
				try {
					msg = (SendMsg) messages.take();
				} catch (InterruptedException e) {
					log.error("[ChannelCometServlet]消息取出失败", e);
				}
				try {
					if (msg == null) {
						synchronized (messages) {
							messages.wait();
						}
					}
				} catch (InterruptedException e) {
				}

				synchronized (connections) {
					synchronized (messages) {

						Long channelId = msg.getChannelId();
						List<HttpServletResponse> responses = connections.get(channelId);

						// 推送消息队列中的消息
						log("Send message: " + msg.getContent() + " to everyone in Channel " + channelId + ".");
						if (responses != null && !responses.isEmpty()) {
							for (HttpServletResponse response : responses) {
								try {
									PrintWriter writer = response.getWriter();
									writer.print("<script type=\"text/javascript\">");
									writer.println("comet.newMessage('" + msg.getContent() + "');");
									writer.print("</script>");
									writer.flush();
								} catch (IOException e) {
									log("IOExeption execute command", e);
								}
							}
						}
					}
				}
			}
		}
	}
}
