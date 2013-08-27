package com.weixin.server.filter.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.weixin.server.filter.Filter;
import com.weixin.server.message.request.InMessage;

/**
 * 数字过滤器
 */
public class NumFilter implements Filter {

	private Pattern p = Pattern.compile("^\\d+$");

	@Override
	public boolean doFilter(InMessage msg) {
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
