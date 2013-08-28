package com.barrage.processer;

import org.springframework.web.context.WebApplicationContext;

import com.weixin.server.message.request.InMessage;
import com.weixin.server.processor.Processor;

public class JoinChannelProcesser implements Processor {

	public void init(WebApplicationContext context) {
	}

	public Object process(InMessage msg) {
		return "Test2";
	}
}
