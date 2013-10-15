package com.barrage.web.server.validator;

import java.util.ArrayList;
import java.util.List;

import com.barrage.web.server.message.request.InMessage;
import com.barrage.web.server.message.response.OutMessage;

public class ValidatorChain {

	private List<Validator> list = new ArrayList<Validator>();

	/**
	 * 服务器构建时调用
	 * @param type
	 */
	public void addValidator(String type) {
		try {
			Validator validator = (Validator) Class.forName(type).newInstance();
			list.add(validator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 验证
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean doValidate(InMessage request, OutMessage response) {
		for (Validator v : list) {
			if (!v.doValidate(request, response)) {
				return false;
			}
		}
		return true;
	}
}
