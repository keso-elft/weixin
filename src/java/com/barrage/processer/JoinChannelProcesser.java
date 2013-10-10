package com.barrage.processer;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.model.Channel;
import com.barrage.service.ChannelService;
import com.barrage.service.UserChannelRelationService;
import com.weixin.common.UserCacheManager;
import com.weixin.server.message.request.InMessage;
import com.weixin.server.model.ProcessError;
import com.weixin.server.processor.Processor;

public class JoinChannelProcesser implements Processor {

	ChannelService channelManager;

	UserCacheManager fansManager;

	UserChannelRelationService userChannelRelationManager;

	public void init(WebApplicationContext context) {
		channelManager = (ChannelService) context.getBean("channelManager");
		fansManager = (UserCacheManager) context.getBean("weiXinFansManager");
		userChannelRelationManager = (UserChannelRelationService) context.getBean("userChannelRelationManager");
	}

	public Object process(InMessage msg) {
		String content = msg.getContent();
		String fromUserName = msg.getFromUserName();

		String[] paras = content.split(" ");
		if (paras != null && paras.length > 1) {
			Long id;
			try {
				id = new Long(paras[1]);
			} catch (Exception e) {
				return new ProcessError("频道号错误");
			}
			Channel channel = channelManager.findChannelById(id);
			if (channel == null)
				return new ProcessError("无此频道");
			if (channel.getPassword() != null && channel.getPassword().length() != 0) {
				if (paras.length == 2)
					return new ProcessError("频道需要密码加入");
				if (paras.length > 2 && !channel.getPassword().equals(paras[2])) {
					return new ProcessError("密码错误");
				}
			}

			userChannelRelationManager.join(fromUserName, channel.getId());
			return null;
		} else {
			return new ProcessError("命令错误");
		}
	}
}
