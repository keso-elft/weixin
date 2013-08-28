package com.barrage.task;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.weixin.common.WeiXinFansManager;
import com.weixin.httpsend.WeixinHttpSender;
import com.weixin.server.session.SessionManager;

public class Task2 {

	protected Logger log = LogManager.getLogger("weixinServer");

	SessionManager sessionManager;

	WeiXinFansManager userManager;

	WeixinHttpSender weixinSender;

	public void run() {
		log.info("task2 notified");
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public WeiXinFansManager getUserManager() {
		return userManager;
	}

	public void setUserManager(WeiXinFansManager userManager) {
		this.userManager = userManager;
	}

	public WeixinHttpSender getWeixinSender() {
		return weixinSender;
	}

	public void setWeixinSender(WeixinHttpSender weixinSender) {
		this.weixinSender = weixinSender;
	}

}
