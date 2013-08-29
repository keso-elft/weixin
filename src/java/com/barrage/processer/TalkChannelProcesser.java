package com.barrage.processer;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.service.ChannelManager;
import com.barrage.service.UserChannelRelationManager;
import com.weixin.common.WeiXinFans;
import com.weixin.common.WeiXinFansManager;
import com.weixin.httpsend.WeixinHttpSender;
import com.weixin.server.message.request.InMessage;
import com.weixin.server.processor.Processor;

public class TalkChannelProcesser implements Processor {

	ChannelManager channelManager;

	WeiXinFansManager fansManager;

	UserChannelRelationManager userChannelRelationManager;

	WeixinHttpSender weixinSender;

	public void init(WebApplicationContext context) {
		channelManager = (ChannelManager) context.getBean("channelManager");
		fansManager = (WeiXinFansManager) context.getBean("fansManager");
		userChannelRelationManager = (UserChannelRelationManager) context.getBean("userChannelRelationManager");
		weixinSender = (WeixinHttpSender) context.getBean("weixinSender");
	}

	public Object process(InMessage msg) {

		WeiXinFans fan = fansManager.getUser(msg.getFromUserName());

		weixinSender.send(msg.getContent());
		return null;
	}
}
