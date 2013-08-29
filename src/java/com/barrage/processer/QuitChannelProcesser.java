package com.barrage.processer;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.service.ChannelManager;
import com.barrage.service.UserChannelRelationManager;
import com.weixin.common.WeiXinFans;
import com.weixin.common.WeiXinFansManager;
import com.weixin.server.message.request.InMessage;
import com.weixin.server.model.ProcessError;
import com.weixin.server.processor.Processor;

public class QuitChannelProcesser implements Processor {

	ChannelManager channelManager;

	WeiXinFansManager fansManager;

	UserChannelRelationManager userChannelRelationManager;

	public void init(WebApplicationContext context) {
		channelManager = (ChannelManager) context.getBean("channelManager");
		fansManager = (WeiXinFansManager) context.getBean("fansManager");
		userChannelRelationManager = (UserChannelRelationManager) context.getBean("userChannelRelationManager");
	}

	public Object process(InMessage msg) {
		String fromUserName = msg.getFromUserName();
		WeiXinFans fan = fansManager.getUser(fromUserName);

		if (fan != null) {
			if (userChannelRelationManager.quit(fan.getId()))
				return "";
			else
				return new ProcessError("退出失败");
		} else {
			return new ProcessError("账号信息有误");
		}
	}
}
