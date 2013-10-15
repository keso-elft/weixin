package com.barrage.web.server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.WebApplicationContext;

import com.barrage.web.server.message.request.InMessage;
import com.barrage.web.server.model.ProcessResult;

/**
 * 服务判定步骤类
 */
public class ServiceStep {

	/**
	 * 步骤编号
	 */
	protected String no;

	/**
	 * 步骤包含的所有选项
	 */
	protected List<ServiceChoise> choiseList;

	/**
	 * 服务初始化
	 * 
	 * @param context
	 */
	public void init(WebApplicationContext context) {
		for (ServiceChoise choise : choiseList) {
			choise.init(context);
		}
	}

	/**
	 * 执行步骤
	 */
	public ProcessResult doStep(InMessage msg) {
		ProcessResult result = null;
		for (ServiceChoise choise : choiseList) {
			result = choise.doChoise(msg);
			if (result != null)
				return result;
		}
		return result;
	}

	/**
	 * 构建时使用
	 */
	public void addChoise(ServiceChoise choise) {
		if (choiseList == null)
			choiseList = new ArrayList<ServiceChoise>();
		choiseList.add(choise);
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
}
