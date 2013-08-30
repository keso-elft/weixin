package com.barrage.dao;

import java.io.Serializable;
import java.util.List;

import com.weixin.common.WeiXinFans;

public interface UserDao extends EntityDao<WeiXinFans, Serializable> {

	public WeiXinFans getUserByFromUserName(String fromUserName);

	public List<WeiXinFans> getAllValidUser();

}
