package com.barrage.service.impl;

import java.util.List;

import com.barrage.dao.UserDao;
import com.barrage.model.User;
import com.barrage.service.UserService;

public class UserServiceImpl implements UserService {

	private UserDao userDao;

	@Override
	public User getUserByFromUserName(String fromUserName) {
		return userDao.getUserByFromUserName(fromUserName);
	}

	@Override
	public List<User> getAllValidUser() {
		return userDao.getAllValidUser();
	}

	@Override
	public void createUser(String fromUserName) {
		userDao.createUser(fromUserName);
	}

	@Override
	public void saveUser(User user) {
		userDao.saveUser(user);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
