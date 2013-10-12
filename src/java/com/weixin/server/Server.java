package com.weixin.server;

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
import com.barrage.service.ChannelService;
import com.barrage.service.UserChannelRelationService;
import com.barrage.worker.WeixinWorker;
import com.weixin.common.UserCacheManager;
import com.weixin.server.message.request.InMessage;
import com.weixin.server.message.response.OutMessage;
import com.weixin.server.model.Result;
import com.weixin.server.service.Service;
import com.weixin.server.service.ServiceManager;
import com.weixin.server.session.Session;
import com.weixin.server.session.SessionManager;
import com.weixin.util.WeixinTools;

public class Server {

	protected Logger log = LogManager.getLogger("weixinServer");

	public ServiceManager serviceManager;

	public SessionManager sessionManager;

	private WeixinWorker weixinWorker;

	public UserCacheManager userManager;

	private ChannelService channelManager;

	private UserChannelRelationService userChannelRelationManager;

	/**
	 * 服务器初始化,各个缓存、线程初始化
	 * @param config
	 */
	public void init(FilterConfig config) {
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(config
				.getServletContext());

		serviceManager = ServiceManager.getInstance();
		sessionManager = (SessionManager) context.getBean("sessionManager");

		weixinWorker = (WeixinWorker) context.getBean("weixinWorker");
		weixinWorker.init();
		userManager = (UserCacheManager) context.getBean("weiXinFansManager");
		userManager.init();
		channelManager = (ChannelService) context.getBean("channelManager");
		channelManager.init();
		userChannelRelationManager = (UserChannelRelationService) context.getBean("userChannelRelationManager");
		userChannelRelationManager.init();

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
		Result result = null;

		// 构造user,session
		String fromUserName = in.getFromUserName();
		User user = userManager.getUser(fromUserName);
		if (user == null) {
			user = userManager.accessUser(fromUserName);
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
		// 在长期service中查询
		if (result == null) {
			for (Service temp : serviceManager.getCommonServices()) {
				service = temp;
				result = service.doService(in);
				if (result != null)
					break;
			}
		}

		if (result != null && result.getResultCode() == Result.RESULT_SUCCESS) {
			// 判断result
			// 判断stepNo前进
			if (result.getServiceName() == null && result.getStepNo() != null) {
				result.setServiceName(service.getServiceName());
			}
			session.setServiceName(result.getServiceName());
			session.setStepNo(result.getStepNo());

			// 消息返回
			OutMessage out = result.getMessage();
			if (out != null) {
				out.setCreateTime(new Date().getTime());
				out.setToUserName(in.getFromUserName());
				out.setFromUserName(in.getToUserName());

				WeixinTools.printOutMessage(out, httpResp);
			}
		} else if (result != null && result.getResultCode() == Result.RESULT_RETRY) {
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
}
