package com.barrage.service.impl;

import java.io.Serializable;

import com.barrage.dao.UserDao;
import com.barrage.service.UserManager;
import com.weixin.common.WeiXinFans;

public class UserManagerImpl extends EntityManagerImpl<WeiXinFans, Serializable> implements UserManager {

	UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
