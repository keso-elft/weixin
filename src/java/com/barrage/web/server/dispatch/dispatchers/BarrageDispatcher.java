package com.barrage.web.server.dispatch.dispatchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.barrage.util.PatternUtil;
import com.barrage.web.server.dispatch.Dispatcher;
import com.barrage.web.server.message.request.InMessage;

public class BarrageDispatcher implements Dispatcher {

	private String CONTENT_PATTERN = ".*";

	private String PASSWORD_PATTERN = ".*";

	private String NUM_PATTERN = ".*";;

	private String pattern;

	@Override
	public boolean doDispatch(InMessage msg) {
		String content = msg.getContent();

		boolean result = false;
		if (pattern != null) {
			Pattern p = Pattern.compile(pattern);
			result = p.matcher(content).find();
		}

		return result;
	}

	@Override
	public void setPara(String para) {
		this.pattern = para;

		// 替换汉字为UNICODE
		// pattern = replaceChinese(pattern);

		// 替换[content]等标示符
		pattern = pattern.replace("[title]", CONTENT_PATTERN);
		pattern = pattern.replace("[content]", CONTENT_PATTERN);
		pattern = pattern.replace("[no]", NUM_PATTERN);
		pattern = pattern.replace("[password]", PASSWORD_PATTERN);

		pattern = "^" + pattern + "$";
	}

	public String replaceChinese(String orgin) {
		String result = orgin;
		String regex = "([\u4e00-\u9fa5]+)";
		Matcher matcher = Pattern.compile(regex).matcher(orgin);
		if (matcher.find()) {
			for (int i = 0; i < matcher.groupCount(); i++) {
				String origin = matcher.group(i);
				String replacement = PatternUtil.toUnicodePattern(origin);
				result = result.replace(origin, replacement);
			}
		}
		return result;
	}

	public static void main(String[] args) {
		BarrageDispatcher filter = new BarrageDispatcher();
		filter.setPara("[no] [password]");

		InMessage msg = new InMessage();
		msg.setContent("");

		System.out.println(filter.doDispatch(msg));
	}
}
