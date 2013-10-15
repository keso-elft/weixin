package com.barrage.web.server.validator;

import com.barrage.web.server.message.request.InMessage;
import com.barrage.web.server.message.response.OutMessage;

public interface Validator {

	public boolean doValidate(InMessage request, OutMessage response);

}
