package com.barrage.processer;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.service.UserChannelRelationManager;
import com.barrage.worker.WeixinWorker;
import com.weixin.send.SendMsg;
import com.weixin.send.Sender;
import com.weixin.server.message.request.InMessage;
import com.weixin.server.model.ProcessError;
import com.weixin.server.processor.Processor;

public class TalkChannelProcesser implements Processor {

	UserChannelRelationManager userChannelRelationManager;

	Sender weixinWorker;

	public void init(WebApplicationContext context) {
		userChannelRelationManager = (UserChannelRelationManager) context.getBean("userChannelRelationManager");
		weixinWorker = (WeixinWorker) context.getBean("weixinWorker");
	}

	public Object process(InMessage msg) {
		String fromUserName = msg.getFromUserName();
		Long channelId = userChannelRelationManager.getJoinChannelId(fromUserName);
		if (channelId != null)
			weixinWorker.send(new SendMsg(fromUserName, channelId, msg.getContent()));
		else
			return new ProcessError("您尚未加入频道");
		return null;
	}
}
