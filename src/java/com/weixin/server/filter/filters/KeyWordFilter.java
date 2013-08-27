package com.weixin.server.filter.filters;

import com.weixin.server.filter.Filter;
import com.weixin.server.message.request.InMessage;

/**
 * 关键词过滤器
 */
public class KeyWordFilter implements Filter {

	private String[] keyWords;

	@Override
	public boolean doFilter(InMessage msg) {
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
		KeyWordFilter filter = new KeyWordFilter();
		filter.setPara("");

		InMessage msg = new InMessage();
		msg.setContent("");

		System.out.println(filter.doFilter(msg));
	}
}
