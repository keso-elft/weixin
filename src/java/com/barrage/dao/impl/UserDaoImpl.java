package com.barrage.dao.impl;

import java.util.Date;
import java.util.List;

import com.barrage.dao.UserDao;
import com.weixin.common.User;

public class UserDaoImpl extends SuperDao implements UserDao {

	@Override
	public User getUserByFromUserName(String fromUserName) {
		@SuppressWarnings("unchecked")
		List<User> list = getHibernateTemplate().find("from WeiXinFans where fromUserName = ?", fromUserName);

		User user = null;
		if (list != null && list.size() > 0) {
			user = list.get(0);
		}
		return user;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getAllValidUser() {
		return getHibernateTemplate().find("from WeiXinFans");
	}

	@Override
	public void createUser(String fromUserName) {
		User user = new User();
		user.setFromUserName(fromUserName);
		user.setLastAccessedTime(new Date());
		getHibernateTemplate().save(user);
	}
}