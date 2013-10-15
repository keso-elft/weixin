package com.barrage.web.server.processer.processeres;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.service.UserChannelRelationService;
import com.barrage.web.server.message.request.InMessage;
import com.barrage.web.server.model.ProcessResult;
import com.barrage.web.server.processer.Processer;

public class QuitChannelProcesser implements Processer {

	UserChannelRelationService userChannelRelationService;

	public void init(WebApplicationContext context) {
		userChannelRelationService = (UserChannelRelationService) context.getBean("userChannelRelationService");
	}

	public ProcessResult process(InMessage msg) {

		ProcessResult result = new ProcessResult();

		String fromUserName = msg.getFromUserName();
		userChannelRelationService.quit(fromUserName);

		return result.success(null);
	}
}
