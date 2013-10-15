package com.barrage.dao.impl;

import java.util.Date;
import java.util.List;

import com.barrage.dao.UserDao;
import com.barrage.model.User;

public class UserDaoImpl extends SuperDao implements UserDao {

	@Override
	public User getUserByFromUserName(String fromUserName) {
		@SuppressWarnings("unchecked")
		List<User> list = getHibernateTemplate().find("from User where fromUserName = ?", fromUserName);

		User user = null;
		if (list != null && list.size() > 0) {
			user = list.get(0);
		}
		return user;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getAllValidUser() {
		return getHibernateTemplate().find(" from User");
	}

	@Override
	public void createUser(String fromUserName) {
		User user = new User();
		user.setFromUserName(fromUserName);
		user.setLastAccessedTime(new Date());
		getHibernateTemplate().save(user);
	}
	
	@Override
	public void saveUser(User user) {
		getHibernateTemplate().saveOrUpdate(user);
	}
}