package com.barrage.processer;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.service.UserChannelRelationService;
import com.weixin.server.message.request.InMessage;
import com.weixin.server.processor.Processor;

public class QuitChannelProcesser implements Processor {

	UserChannelRelationService userChannelRelationManager;

	public void init(WebApplicationContext context) {
		userChannelRelationManager = (UserChannelRelationService) context.getBean("userChannelRelationManager");
	}

	public Object process(InMessage msg) {
		String fromUserName = msg.getFromUserName();
		userChannelRelationManager.quit(fromUserName);
		return null;
	}
}
