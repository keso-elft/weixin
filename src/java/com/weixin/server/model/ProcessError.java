package com.weixin.server.model;

/**
 * Processor 执行返回错误
 */
public class ProcessError {

	String errorMsg;

	public ProcessError(String errorMsg) {
		super();
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
