package com.barrage.processer;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.service.UserChannelRelationManager;
import com.weixin.httpsend.WeixinHttpSender;
import com.weixin.server.message.request.InMessage;
import com.weixin.server.processor.Processor;

public class TalkChannelProcesser implements Processor {

	UserChannelRelationManager userChannelRelationManager;

	WeixinHttpSender weixinSender;

	public void init(WebApplicationContext context) {
		userChannelRelationManager = (UserChannelRelationManager) context.getBean("userChannelRelationManager");
		weixinSender = (WeixinHttpSender) context.getBean("weixinSender");
	}

	public Object process(InMessage msg) {
		String fromUserName = msg.getFromUserName();
		Long channelId = userChannelRelationManager.getJoinChannelId(fromUserName);
		if (channelId != null)
			weixinSender.send(fromUserName, channelId, msg.getContent());
		return null;
	}
}
