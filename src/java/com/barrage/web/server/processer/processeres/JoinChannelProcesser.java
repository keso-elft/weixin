package com.barrage.web.server.processer.processeres;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.model.Channel;
import com.barrage.service.ChannelService;
import com.barrage.service.UserChannelRelationService;
import com.barrage.web.server.message.request.InMessage;
import com.barrage.web.server.model.ProcessResult;
import com.barrage.web.server.processer.Processer;

public class JoinChannelProcesser implements Processer {

	ChannelService channelService;

	UserChannelRelationService userChannelRelationService;

	public void init(WebApplicationContext context) {
		channelService = (ChannelService) context.getBean("channelService");
		userChannelRelationService = (UserChannelRelationService) context.getBean("userChannelRelationService");
	}

	public ProcessResult process(InMessage msg) {

		ProcessResult result = new ProcessResult();

		String content = msg.getContent();
		String fromUserName = msg.getFromUserName();

		String[] paras = content.split(" ");
		if (paras != null && paras.length > 0) {
			Long id;
			try {
				id = new Long(paras[0]);
			} catch (Exception e) {
				return result.error("频道号错误");
			}
			Channel channel = channelService.findChannelById(id);
			if (channel == null) {
				return result.error("无此频道");
			}
			if (channel.getPassword() != null && channel.getPassword().length() != 0) {
				if (paras.length == 1) {
					return result.error("频道需要密码加入");
				}
				if (paras.length > 1 && !channel.getPassword().equals(paras[1])) {
					return result.error("密码错误");
				}
			}

			userChannelRelationService.join(fromUserName, channel.getId());
			return result.success(null);
		} else {
			return result.error("命令错误");
		}
	}
}
