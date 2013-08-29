package com.barrage.processer;

import java.util.Date;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.model.Channel;
import com.barrage.service.ChannelManager;
import com.weixin.common.WeiXinFans;
import com.weixin.common.WeiXinFansManager;
import com.weixin.server.message.request.InMessage;
import com.weixin.server.model.ProcessError;
import com.weixin.server.processor.Processor;

public class CreateChannelProcesser implements Processor {

	ChannelManager channelManager;

	WeiXinFansManager fansManager;

	public void init(WebApplicationContext context) {
		channelManager = (ChannelManager) context.getBean("channelManager");
		fansManager = (WeiXinFansManager) context.getBean("fansManager");
	}

	public Object process(InMessage msg) {
		String content = msg.getContent();
		String fromUserName = msg.getFromUserName();
		WeiXinFans fan = fansManager.getUser(fromUserName);

		String[] paras = content.split(" ");
		if (fan != null && paras != null && paras.length > 1) {
			Channel channel = new Channel();
			channel.setName(paras[1]);
			if (paras.length > 2)
				channel.setPassword(paras[2]);
			channel.setCreateUserId(fan.getId());
			channel.setCreateTime(new Date());

			String result = channelManager.save(channel).toString();

			if (result.equalsIgnoreCase("-1"))
				return new ProcessError("创建失败");

			return result;
		} else {
			return new ProcessError("创建失败");
		}
	}
}
