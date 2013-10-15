package com.barrage.web.server;

import java.util.Collection;
import java.util.Date;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.barrage.model.User;
import com.barrage.web.output.WeixinOutputer;
import com.barrage.web.server.cache.UserCacheManager;
import com.barrage.web.server.message.request.InMessage;
import com.barrage.web.server.message.response.OutMessage;
import com.barrage.web.server.model.ProcessResult;
import com.barrage.web.server.service.Service;
import com.barrage.web.server.service.ServiceManager;
import com.barrage.web.server.session.Session;
import com.barrage.web.server.session.SessionManager;
import com.barrage.web.util.WeixinTools;

public class Server {

	protected Logger log = LogManager.getLogger("weixinServer");

	private ServiceManager serviceManager;

	private SessionManager sessionManager;

	private WeixinOutputer weixinOutputer;

	private UserCacheManager userCacheManager;

	/**
	 * 服务器初始化,各个缓存、线程初始化
	 * @param config
	 */
	public void init(FilterConfig config) {
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(config
				.getServletContext());

		serviceManager = ServiceManager.getInstance();
		sessionManager = (SessionManager) context.getBean("sessionManager");

		weixinOutputer = (WeixinOutputer) context.getBean("weixinOutputer");
		weixinOutputer.init();
		userCacheManager = (UserCacheManager) context.getBean("userCacheManager");
		userCacheManager.init();

		Collection<Service> services = serviceManager.getServices();
		for (Service service : services) {
			service.init(context);
		}
	}

	/**
	 * 处理请求
	 * @param httpReq
	 * @param httpResp
	 */
	public void doServer(HttpServletRequest httpReq, HttpServletResponse httpResp) {

		// 构造消息
		InMessage in = WeixinTools.parseRequest(httpReq);
		ProcessResult result = null;

		// 构造user,session
		String fromUserName = in.getFromUserName();
		User user = userCacheManager.getUser(fromUserName);
		if (user == null) {
			user = userCacheManager.accessUser(fromUserName);
		}
		sessionManager.accessSession(user);
		Session session = sessionManager.getSession(fromUserName);

		Service service = null;
		// 先在session保存的上一次进度中查询
		if (session.getServiceName() != null) {
			service = serviceManager.getService(session.getServiceName());
			if (service != null) {
				result = service.doService(in, session.getStepNo());
			}
		}
		// 再在长期service中查询
		if (result == null) {
			for (Service temp : serviceManager.getCommonServices()) {
				service = temp;
				result = service.doService(in);
				if (result != null)
					break;
			}
		}

		// 将返回结果中的步骤设置到session缓存
		if (result != null && result.isSuccess()) {
			if (result.getServiceName() == null && result.getStepNo() != null) {
				result.setServiceName(service.getServiceName());
			}
			session.setServiceName(result.getServiceName());
			session.setStepNo(result.getStepNo());
		}
		if (result != null) {
			// 消息返回
			OutMessage out = result.getMessage();
			if (out != null) {
				out.setCreateTime(new Date().getTime());
				out.setToUserName(in.getFromUserName());
				out.setFromUserName(in.getToUserName());

				WeixinTools.printOutMessage(out, httpResp);
			}
		} else {
			// TODO 未处理消息
			log.info("no service enter");
		}
	}

	public WeixinOutputer getWeixinOutputer() {
		return weixinOutputer;
	}

	public void setWeixinOutputer(WeixinOutputer weixinOutputer) {
		this.weixinOutputer = weixinOutputer;
	}

	public UserCacheManager getUserCacheManager() {
		return userCacheManager;
	}

	public void setUserCacheManager(UserCacheManager userCacheManager) {
		this.userCacheManager = userCacheManager;
	}
}
