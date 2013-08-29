package com.weixin.server.model;

import com.weixin.server.message.response.OutMessage;

/**
 * 处理返回的结果
 */
public class Result {

	public static final int RESULT_SUCCESS = 1;

	public static final int RESULT_FAIL = 2;
	
	public static final int RESULT_RETRY = 3;

	/**
	 * 结果码
	 */
	private int resultCode;

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

	public Result() {
		super();
	}

	public Result(int resultCode) {
		super();
		this.resultCode = resultCode;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public OutMessage getMessage() {
		return message;
	}

	public void setMessage(OutMessage message) {
		this.message = message;
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
