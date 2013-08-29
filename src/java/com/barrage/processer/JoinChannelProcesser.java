package com.barrage.processer;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.model.Channel;
import com.barrage.service.ChannelManager;
import com.barrage.service.UserChannelRelationManager;
import com.weixin.common.WeiXinFans;
import com.weixin.common.WeiXinFansManager;
import com.weixin.server.message.request.InMessage;
import com.weixin.server.processor.Processor;

public class JoinChannelProcesser implements Processor {

	ChannelManager channelManager;

	WeiXinFansManager fansManager;

	UserChannelRelationManager userChannelRelationManager;

	public void init(WebApplicationContext context) {
		channelManager = (ChannelManager) context.getBean("channelManager");
		fansManager = (WeiXinFansManager) context.getBean("fansManager");
		userChannelRelationManager = (UserChannelRelationManager) context.getBean("userChannelRelationManager");
	}

	public Object process(InMessage msg) {
		String content = msg.getContent();
		String fromUserName = msg.getFromUserName();
		WeiXinFans fan = fansManager.getUser(fromUserName);

		String[] paras = content.split(" ");
		if (fan != null && paras != null && paras.length > 1) {
			Long id = new Long(paras[1]);
			Channel channel = channelManager.get(id);
			if (channel != null && channel.getPassword() != null && channel.getPassword().length() != 0) {
				if (paras.length == 2)
					return null;
				if (paras.length > 2) {
					if (!channel.getPassword().equals(paras[2])) {
						return null;
					}
				}
			}

			return userChannelRelationManager.join(fan.getId(), channel.getId());
		} else {
			return null;
		}
	}
}
