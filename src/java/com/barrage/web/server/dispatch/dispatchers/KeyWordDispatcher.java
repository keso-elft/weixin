package com.barrage.web.server.dispatch.dispatchers;

import com.barrage.web.server.dispatch.Dispatcher;
import com.barrage.web.server.message.request.InMessage;

/**
 * 关键词过滤器
 */
public class KeyWordDispatcher implements Dispatcher {

	private String[] keyWords;

	@Override
	public boolean doDispatch(InMessage msg) {
		String content = msg.getContent();
		for (String keyword : keyWords) {
			if (content.contains(keyword)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void setPara(String para) {
		keyWords = para.split(",");
	}

	public static void main(String[] args) {
		KeyWordDispatcher filter = new KeyWordDispatcher();
		filter.setPara("");

		InMessage msg = new InMessage();
		msg.setContent("");

		System.out.println(filter.doDispatch(msg));
	}
}
