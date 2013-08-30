package com.barrage.processer;

import org.springframework.web.context.WebApplicationContext;

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
		fansManager = (WeiXinFansManager) context.getBean("weiXinFansManager");
	}

	public Object process(InMessage msg) {
		String content = msg.getContent();
		String fromUserName = msg.getFromUserName();
		WeiXinFans fan = fansManager.getUser(fromUserName);
		if (fan == null)
			return new ProcessError("找不到该用户");

		String[] paras = content.split(" ");
		if (paras != null && paras.length > 1) {
			String password = null;
			if (paras.length > 2)
				password = paras[2];
			Long result = channelManager.createChannel(fan.getId(), paras[1], password);
			if (result.longValue() == -1)
				return new ProcessError("创建失败");
			return result;
		} else {
			return new ProcessError("命令错误");
		}
	}
}
