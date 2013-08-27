package com.weixin.server.filter;

import com.weixin.server.message.request.InMessage;

public interface Filter {

	/**
	 * 设置过滤字段
	 */
	public void setPara(String para);

	/**
	 * 判断过滤条件
	 */
	public boolean doFilter(InMessage msg);

}
