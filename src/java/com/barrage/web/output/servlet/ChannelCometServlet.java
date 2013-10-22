package com.barrage.web.output.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
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
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.barrage.model.Channel;
import com.barrage.service.ChannelService;
import com.barrage.web.output.SendMsg;

public class ChannelCometServlet extends HttpServlet implements CometProcessor {

	protected Logger log = LogManager.getLogger("weixinOutput");

	private static final long serialVersionUID = -3667180332947986301L;

	// <频道号,List<长连接>>
	protected static Map<Long, Map<String, HttpServletResponse>> connections = new HashMap<Long, Map<String, HttpServletResponse>>();

	// 消息推送线程
	protected static ChannelCometSender channelSender = null;

	ApplicationContext context;

	private ChannelService channelService;

	public void init() throws ServletException {
		// 启动消息推送线程
		channelSender = new ChannelCometSender();
		Thread channelSenderThread = new Thread(channelSender, "ChannelCometSender["
				+ getServletContext().getContextPath() + "]");
		channelSenderThread.setDaemon(true);
		channelSenderThread.start();

		context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		channelService = (ChannelService) context.getBean("channelService");
	}

	public void destroy() {
		connections.clear();
		channelSender.stop();
		channelSender = null;
	}

	public void event(CometEvent event) throws IOException, ServletException {
		HttpServletRequest request = event.getHttpServletRequest();
		HttpServletResponse response = event.getHttpServletResponse();

		response.setHeader("Cache-Control", "private");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		// 频道号
		Long channelId = null;
		String password = null;
		try {
			channelId = Long.parseLong(request.getParameter("channelId"));
			password = request.getParameter("password");
		} catch (Exception e) {
		}
		if (channelId == null) {
			return;
		}

		if (event.getEventType() == CometEvent.EventType.BEGIN) {

			// 判断登录信息
			Channel channel = channelService.findChannelById(channelId);
			if (channel != null && channel.getPassword().equals(password)) {
				log("[ChannelCometServlet] login success");
			} else {
				log("[ChannelCometServlet] login fail");
				return;
			}

			log("[ChannelCometServlet] New session: " + request.getSession(true).getId());
			event.setTimeout(Integer.MAX_VALUE);

			join(channelId, request.getSession(true).getId(), response);

			// TODO IE8没问题,FF有问题
			// 欢迎信息
			PrintWriter writer = response.getWriter();

			writer.println("<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">");
			writer.println("<html><head><script type=\"text/javascript\">var comet = window.parent.comet;</script></head><body>");
			writer.println("<script type=\"text/javascript\">");
			writer.println("var comet = window.parent.comet;");
			writer.println("</script>");
			writer.print("<script type=\"text/javascript\">");
			writer.println("comet.newMessage('Welcome to Channel 1 radio!');");
			writer.print("</script>");
			writer.flush();

		} else if (event.getEventType() == CometEvent.EventType.ERROR) {

			log("[ChannelCometServlet] Error session: " + request.getSession(true).getId());

			quit(channelId, request.getSession(true).getId());
			event.close();

		} else if (event.getEventType() == CometEvent.EventType.END) {

			// TODO 退出时会执行两次
			log("[ChannelCometServlet] End session: " + request.getSession(true).getId());

			quit(channelId, request.getSession(true).getId());
			PrintWriter writer = response.getWriter();
			writer.println("</body></html>");
			writer.flush();
			event.close();

		} else if (event.getEventType() == CometEvent.EventType.READ) {

			log("[ChannelCometServlet] Read session: " + request.getSession(true).getId());

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

	private void join(Long channelId, String sessionId, HttpServletResponse response) {
		Map<String, HttpServletResponse> temp = connections.get(channelId);
		if (temp == null) {
			temp = new HashMap<String, HttpServletResponse>();
		}
		temp.put(sessionId, response);
		synchronized (connections) {
			connections.put(channelId, temp);
		}
	}

	private void quit(Long channelId, String sessionId) {

		Map<String, HttpServletResponse> temp = connections.get(channelId);
		if (temp == null) {
			return;
		}
		temp.remove(sessionId);

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
						Map<String, HttpServletResponse> responses = connections.get(channelId);

						// 推送消息队列中的消息
						if (responses != null && !responses.isEmpty()) {
							log("Send message: '" + msg.getContent() + "' to everyone in Channel " + channelId + ".");

							for (HttpServletResponse response : responses.values()) {
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
						} else {
							log.info("[ChannelCometServlet]消息发送:目前频道 " + channelId + " 无网页登陆用户");
						}
					}
				}
			}
		}
	}
}
