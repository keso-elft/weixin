package com.weixin.server.validator;

import com.weixin.server.message.request.InMessage;
import com.weixin.server.message.response.OutMessage;

public interface Validator {

	public boolean doValidate(InMessage request, OutMessage response);

}
