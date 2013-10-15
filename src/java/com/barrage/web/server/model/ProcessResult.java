package com.barrage.web.server.model;

import java.util.List;

import com.barrage.web.server.message.response.OutMessage;

/**
 * 处理返回的结果
 */
public class ProcessResult {

	public static final int RESULT_SUCCESS = 1;

	public static final int RESULT_FAIL = 2;

	public static final int RESULT_RETRY = 3;

	/**
	 * 结果码
	 */
	private int resultCode;

	/**
	 * 结果数据
	 */
	private Object result;

	/**
	 * 返回消息
	 */
	private OutMessage message;

	/**
	 * 下步处理Service名称
	 */
	private String serviceName;

	/**
	 * 下步处理Service步骤编号
	 */
	private String stepNo;

	/**
	 * 成功
	 * @param result
	 */
	public ProcessResult success(Object result) {
		this.resultCode = RESULT_SUCCESS;
		this.result = result;
		return this;
	}

	/**
	 * 错误
	 * @param error
	 */
	public ProcessResult error(String error) {
		this.resultCode = RESULT_RETRY;
		this.result = error;
		return this;
	}

	/**
	 * 生成消息
	 * @param error
	 */
	@SuppressWarnings("rawtypes")
	public void buildMessage(String content) {

		OutMessage message = new OutMessage();

		message.setContent(content);

		if (isSuccess()) {
			if (result instanceof List) {
				List retList = (List) result;
				String temp = "";
				for (int i = 0; i < retList.size(); i++) {
					temp += (i + 1) + "." + retList.get(i) + "\r\n";
				}
				message.setContent(content.replace("[list]", temp));
			} else if (result instanceof String) {
				message.setContent(content.replace("[content]", result.toString()));
			}
		}

		this.message = message;
	}

	public boolean isSuccess() {
		return resultCode == RESULT_SUCCESS;
	}

	public boolean isRetry() {
		return resultCode == RESULT_RETRY;
	}

	public int getResultCode() {
		return resultCode;
	}

	public OutMessage getMessage() {
		return message;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getStepNo() {
		return stepNo;
	}

	public void setStepNo(String stepNo) {
		this.stepNo = stepNo;
	}

}
