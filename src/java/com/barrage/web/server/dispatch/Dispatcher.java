package com.barrage.web.server.dispatch;

import com.barrage.web.server.message.request.InMessage;

public interface Dispatcher {

	/**
	 * 设置过滤字段
	 */
	public void setPara(String para);

	/**
	 * 判断过滤条件
	 */
	public boolean doDispatch(InMessage msg);

}
