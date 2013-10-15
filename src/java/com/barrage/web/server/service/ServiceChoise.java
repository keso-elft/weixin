package com.barrage.web.server.service;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.web.server.dispatch.DispatcherChain;
import com.barrage.web.server.message.request.InMessage;
import com.barrage.web.server.model.ProcessResult;
import com.barrage.web.server.processer.Processer;

/**
 * 服务步骤中的选择项类
 */
public class ServiceChoise {

	/**
	 * 过滤器链
	 */
	protected DispatcherChain chain;

	/**
	 * 执行器
	 */
	protected Processer processor;

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
	public ProcessResult doChoise(InMessage msg) {

		ProcessResult result = null;

		if (chain == null || chain.doDispatch(msg)) {

			result = new ProcessResult();
			if (processor != null) {
				result = processor.process(msg);
			} else {
				result.success(null);
			}
			result.buildMessage(successMsg);

			if (result.isSuccess()) {
				result.setStepNo(toNo);
				result.setServiceName(toService);
			}
		}
		return result;
	}

	public DispatcherChain getChain() {
		return chain;
	}

	public void setChain(DispatcherChain chain) {
		this.chain = chain;
	}

	public Processer getProcessor() {
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
			this.processor = (Processer) Class.forName(type).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
