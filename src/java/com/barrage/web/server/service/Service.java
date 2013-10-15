package com.barrage.web.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;

import com.barrage.web.server.message.request.InMessage;
import com.barrage.web.server.model.ProcessResult;

/**
 * 应答Service
 */
public class Service {

	protected Logger log = LogManager.getLogger("weixinServer");

	protected static final String _startNo = "1";

	/**
	 * Service名称
	 */
	protected String serviceName;

	/**
	 * Service默认首先发送消息
	 */
	protected String serviceSendMessage;

	/**
	 * Service包含的所有判定步骤
	 */
	protected Map<String, ServiceStep> stepMap;

	/**
	 * 子服务列表
	 */
	protected List<Service> subServices;

	/**
	 * 服务初始化
	 */
	public void init(WebApplicationContext context) {
		if (stepMap != null) {
			for (ServiceStep step : stepMap.values()) {
				step.init(context);
			}
		}
		if (subServices != null) {
			for (Service sub : subServices) {
				sub.init(context);
			}
		}
	}

	/**
	 * 服务默认执行
	 */
	public ProcessResult doService(InMessage req) {
		return doService(req, _startNo);
	}

	/**
	 * 服务按指定步骤执行
	 */
	public ProcessResult doService(InMessage req, String stepNo) {
		if (stepNo == null)
			stepNo = _startNo;
		ServiceStep step = stepMap == null ? null : stepMap.get(stepNo);
		if (step != null)
			return step.doStep(req);
		else
			return null;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceSendMessage(String serviceSendMessage) {
		this.serviceSendMessage = serviceSendMessage;
	}

	public String getServiceSendMessage() {
		return serviceSendMessage;
	}

	public void addStep(ServiceStep step) throws Exception {
		if (stepMap == null)
			stepMap = new HashMap<String, ServiceStep>();
		stepMap.put(step.getNo(), step);
	}

	public ServiceStep getService(String stepNo) {
		ServiceStep step = stepMap.get(stepNo);
		return step;
	}

}
