package com.weixin.server.filter;

import java.util.ArrayList;
import java.util.List;

import com.weixin.server.message.request.InMessage;

public class FilterChain {

	private List<Filter> list = new ArrayList<Filter>();

	/**
	 * 服务器构建时调用
	 * @param type
	 */
	public void addFilter(String type, String para) {
		try {
			Filter filter = (Filter) Class.forName(type).newInstance();
			filter.setPara(para);
			list.add(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 验证
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean doFilter(InMessage request) {
		for (Filter f : list) {
			if (!f.doFilter(request)) {
				return false;
			}
		}
		return true;
	}
}
