package com.barrage.task;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.barrage.model.User;
import com.weixin.common.UserCacheManager;
import com.weixin.server.service.ServiceManager;
import com.weixin.server.session.Session;
import com.weixin.server.session.SessionManager;

public class Task1 {

	protected Logger log = LogManager.getLogger("weixinServer");

	SessionManager sessionManager;

	UserCacheManager weiXinFansManager;

	public void run() {

		weiXinFansManager.accessUser("1000");
		List<User> users = weiXinFansManager.getUsers();

		for (User user : users) {
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

	public UserCacheManager getWeiXinFansManager() {
		return weiXinFansManager;
	}

	public void setWeiXinFansManager(UserCacheManager weiXinFansManager) {
		this.weiXinFansManager = weiXinFansManager;
	}

}
