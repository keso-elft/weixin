package com.barrage.web.server.processer.processeres;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;

import com.barrage.model.Channel;
import com.barrage.service.ChannelService;
import com.barrage.service.UserChannelRelationService;
import com.barrage.web.output.SendMsg;
import com.barrage.web.output.Sender;
import com.barrage.web.output.WeixinOutputer;
import com.barrage.web.server.cache.UserCacheManager;
import com.barrage.web.server.message.request.InMessage;
import com.barrage.web.server.model.ProcessResult;
import com.barrage.web.server.processer.Processer;

public class TalkChannelProcesser implements Processer {

	protected Logger log = LogManager.getLogger("weixinServer");

	UserChannelRelationService userChannelRelationService;

	ChannelService channelService;

	UserCacheManager userCacheManager;

	Sender weixinOutputer;

	public void init(WebApplicationContext context) {
		userChannelRelationService = (UserChannelRelationService) context.getBean("userChannelRelationService");
		channelService = (ChannelService) context.getBean("channelService");
		userCacheManager = (UserCacheManager) context.getBean("userCacheManager");
		weixinOutputer = (WeixinOutputer) context.getBean("weixinOutputer");
	}

	public ProcessResult process(InMessage msg) {

		ProcessResult result = new ProcessResult();
		String fromUserName = msg.getFromUserName();

		log.info("[TalkChannelProcesser]" + msg.getContent());

		Long channelId = userChannelRelationService.getJoinChannelId(fromUserName);
		Channel channel = channelService.findChannelById(channelId);

		if (channelId != null) {
			weixinOutputer.send(new SendMsg(userCacheManager.getUser(fromUserName).getId(), channelId,
					msg.getContent(), channel.getOutputType()));
			return result.success(null);
		} else {
			return result.error("您尚未加入频道");
		}
	}
}
