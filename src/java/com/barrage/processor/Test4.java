package com.barrage.processor;

import org.springframework.web.context.WebApplicationContext;

import com.weixin.server.message.request.InMessage;
import com.weixin.server.processor.Processor;

public class Test4 implements Processor {

	public void init(WebApplicationContext context) {
	}

	public Object process(InMessage msg) {
		return "";
	}
}
