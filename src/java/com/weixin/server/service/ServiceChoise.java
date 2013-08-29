package com.weixin.server.service;

import java.util.List;

import org.springframework.web.context.WebApplicationContext;

import com.weixin.server.filter.FilterChain;
import com.weixin.server.message.request.InMessage;
import com.weixin.server.message.response.OutMessage;
import com.weixin.server.model.ProcessError;
import com.weixin.server.model.Result;
import com.weixin.server.processor.Processor;

/**
 * 服务步骤中的选择项类
 */
public class ServiceChoise {

	/**
	 * 过滤器链
	 */
	protected FilterChain chain;

	/**
	 * 执行器
	 */
	protected Processor processor;

	/**
	 * 执行后返回的消息,可拼装
	 */
	protected String successMsg;

	/**
	 * 执行后跳转的步骤号
	 */
	protected String toNo;

	/**
	 * 执行后跳转的Service名称
	 */
	protected String toService;

	/**
	 * 服务初始化
	 * 
	 * @param context
	 */
	public void init(WebApplicationContext context) {
		if (processor != null)
			processor.init(context);
	}

	/**
	 * 服务调用
	 * 
	 * @param msg
	 * @return
	 */
	public Result doChoise(InMessage msg) {
		Result result = null;
		if (chain == null || chain.doFilter(msg)) {
			result = new Result();

			boolean isError = false;

			if (processor != null) {
				Object rtnObj = processor.process(msg);
				if (rtnObj instanceof ProcessError) {
					isError = true;
				}
				result.setMessage(createMessage(processor.process(msg)));
			} else
				result.setMessage(createMessage(successMsg));

			if (!isError) {
				result.setStepNo(toNo);
				result.setServiceName(toService);
				result.setResultCode(Result.RESULT_SUCCESS);
			} else
				result.setResultCode(Result.RESULT_RETRY);
		}
		return result;
	}

	/**
	 * 根据返回,封装消息
	 * @param retObject
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public OutMessage createMessage(Object retObject) {
		OutMessage message = new OutMessage();
		String content = null;

		if (successMsg != null) {
			content = successMsg;
		} else {
			Service service = ServiceManager.getInstance().getService(toService);
			if (service != null)
				content = service.getServiceSendMessage();
		}
		if (content == null)
			return null;

		if (retObject instanceof List) {
			List retList = (List) retObject;
			String temp = "";

			for (int i = 0; i < retList.size(); i++) {
				temp += (i + 1) + "." + retList.get(i) + "\n";
			}
			message.setContent(content.replace("[list]", temp));
		} else if (retObject instanceof String) {
			message.setContent(content.replace("[content]", retObject.toString()));
		} else if (retObject instanceof ProcessError) {
			ProcessError retError = (ProcessError) retObject;
			message.setContent(retError.getErrorMsg());
		} else {
			message.setContent(content);
		}
		return message;
	}

	public FilterChain getChain() {
		return chain;
	}

	public void setChain(FilterChain chain) {
		this.chain = chain;
	}

	public Processor getProcessor() {
		return processor;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public String getToNo() {
		return toNo;
	}

	public void setToNo(String toNo) {
		this.toNo = toNo;
	}

	public String getToService() {
		return toService;
	}

	public void setToService(String toService) {
		this.toService = toService;
	}

	/**
	 * 服务器初始化时调用
	 * 
	 * @param type
	 */
	public void setProcessor(String type) {
		try {
			this.processor = (Processor) Class.forName(type).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
