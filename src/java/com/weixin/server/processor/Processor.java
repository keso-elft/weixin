package com.weixin.server.processor;

import org.springframework.web.context.WebApplicationContext;

import com.weixin.server.message.request.InMessage;

public interface Processor {

	/**
	 * 服务初始化
	 * 
	 * @param context
	 */
	public void init(WebApplicationContext context);

	/**
	 * 服务执行
	 * 
	 * @param msg
	 * @return
	 */
	public Object process(InMessage msg);

}
