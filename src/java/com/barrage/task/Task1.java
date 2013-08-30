package com.barrage.task;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.weixin.common.WeiXinFans;
import com.weixin.common.WeiXinFansManager;
import com.weixin.httpsend.WeixinHttpSender;
import com.weixin.server.service.ServiceManager;
import com.weixin.server.session.Session;
import com.weixin.server.session.SessionManager;

public class Task1 {

	protected Logger log = LogManager.getLogger("weixinServer");

	SessionManager sessionManager;

	WeiXinFansManager weiXinFansManager;

	WeixinHttpSender weixinSender;

	public void run() {

		weiXinFansManager.accessUser("1000");
		List<WeiXinFans> users = weiXinFansManager.getUsers();

		for (WeiXinFans user : users) {
			// 发送
			String content = ServiceManager.getInstance().getService("liveNotify").getServiceSendMessage();
			content = content.replace("[content]", "aaa");
			// weixinSender.send(content);
			// 设置session
			sessionManager.accessSession(user);
			Session session = sessionManager.getSession(user.getFromUserName());
			session.putRecord("serviceName", "liveNotify");
			session.putRecord("stepNo", "1");
		}
		log.info("task1 notified");
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public WeixinHttpSender getWeixinSender() {
		return weixinSender;
	}

	public void setWeixinSender(WeixinHttpSender weixinSender) {
		this.weixinSender = weixinSender;
	}

	public WeiXinFansManager getWeiXinFansManager() {
		return weiXinFansManager;
	}

	public void setWeiXinFansManager(WeiXinFansManager weiXinFansManager) {
		this.weiXinFansManager = weiXinFansManager;
	}

}
