package com.barrage.web.server.dispatch;

import java.util.ArrayList;
import java.util.List;

import com.barrage.web.server.message.request.InMessage;

public class DispatcherChain {

	private List<Dispatcher> list = new ArrayList<Dispatcher>();

	/**
	 * 服务器构建时调用
	 * @param type
	 */
	public void addDispatcher(String type, String para) {
		try {
			Dispatcher filter = (Dispatcher) Class.forName(type).newInstance();
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
	public boolean doDispatch(InMessage request) {
		for (Dispatcher f : list) {
			if (!f.doDispatch(request)) {
				return false;
			}
		}
		return true;
	}
}
