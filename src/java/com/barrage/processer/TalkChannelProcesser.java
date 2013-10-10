package com.barrage.processer;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.service.UserChannelRelationService;
import com.barrage.worker.WeixinWorker;
import com.weixin.send.SendMsg;
import com.weixin.send.Sender;
import com.weixin.server.message.request.InMessage;
import com.weixin.server.model.ProcessError;
import com.weixin.server.processor.Processor;

public class TalkChannelProcesser implements Processor {

	UserChannelRelationService userChannelRelationService;

	Sender weixinWorker;

	public void init(WebApplicationContext context) {
		userChannelRelationService = (UserChannelRelationService) context.getBean("userChannelRelationManager");
		weixinWorker = (WeixinWorker) context.getBean("weixinWorker");
	}

	public Object process(InMessage msg) {
		String fromUserName = msg.getFromUserName();
		Long channelId = userChannelRelationService.getJoinChannelId(fromUserName);
		if (channelId != null)
			weixinWorker.send(new SendMsg(fromUserName, channelId, msg.getContent()));
		else
			return new ProcessError("您尚未加入频道");
		return null;
	}
}
