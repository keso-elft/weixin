package com.barrage.web.server.dispatch.dispatchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.barrage.web.server.dispatch.Dispatcher;
import com.barrage.web.server.message.request.InMessage;

/**
 * 数字过滤器
 */
public class NumDispatcher implements Dispatcher {

	private Pattern p = Pattern.compile("^\\d+$");

	@Override
	public boolean doDispatch(InMessage msg) {
		String content = msg.getContent();

		Matcher matcher = p.matcher(content);
		if (matcher.find())
			return true;

		return false;
	}

	@Override
	public void setPara(String para) {
		p = Pattern.compile("^\\d{" + para + "}$");
	}
}
