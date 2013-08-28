package com.barrage.model;

import java.util.Date;

/**
 * 频道表
 */
public class Channel extends BaseObject {

	private Long id;

	private String name;

	private String desc;

	private Long createUserId;

	private Date createTime;

	private Long status;

	/**
	 * 是否需要保存消息,默认0,0=不保存,1=保存
	 */
	private Long isStore;

	/**
	 * 频道到期时间,null表示永不到期
	 */
	private Date endTime;

	/**
	 * 输出方式,二级制数字,默认1,个位0=不输出到微信界面,1=输出到微信界面, 十位0=不输出到WEB界面,1=输出到WEB界面
	 */
	private Long outputType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getIsStore() {
		return isStore;
	}

	public void setIsStore(Long isStore) {
		this.isStore = isStore;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getOutputType() {
		return outputType;
	}

	public void setOutputType(Long outputType) {
		this.outputType = outputType;
	}

}
