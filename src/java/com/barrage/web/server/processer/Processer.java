package com.barrage.web.server.processer;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.web.server.message.request.InMessage;
import com.barrage.web.server.model.ProcessResult;

public interface Processer {

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
	public ProcessResult process(InMessage msg);

}
