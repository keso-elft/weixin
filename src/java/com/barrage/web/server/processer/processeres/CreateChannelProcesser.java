package com.barrage.web.server.processer.processeres;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.model.User;
import com.barrage.service.ChannelService;
import com.barrage.web.server.cache.UserCacheManager;
import com.barrage.web.server.message.request.InMessage;
import com.barrage.web.server.model.ProcessResult;
import com.barrage.web.server.processer.Processer;

public class CreateChannelProcesser implements Processer {

	ChannelService channelService;

	UserCacheManager userCacheManager;

	public void init(WebApplicationContext context) {
		channelService = (ChannelService) context.getBean("channelService");
		userCacheManager = (UserCacheManager) context.getBean("userCacheManager");
	}

	public ProcessResult process(InMessage msg) {

		ProcessResult result = new ProcessResult();

		String content = msg.getContent();
		String fromUserName = msg.getFromUserName();
		User fan = userCacheManager.getUser(fromUserName);
		if (fan == null) {
			return result.error("找不到该用户");
		}

		String[] paras = content.split(" ");
		if (paras != null && paras.length > 0) {
			String password = null;
			if (paras.length > 1)
				password = paras[1];
			Long channelId = channelService.createChannel(fan.getId(), paras[0], password);
			if (channelId.longValue() == -1) {
				return result.error("创建失败");
			}
			return result.success(channelId.toString());
		} else {
			return result.error("命令错误");
		}
	}
}
