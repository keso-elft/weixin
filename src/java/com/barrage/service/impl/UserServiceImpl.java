package com.barrage.service.impl;

import java.util.List;

import com.barrage.model.User;
import com.barrage.service.UserService;

public class UserServiceImpl implements UserService {

	private UserService userService;

	@Override
	public User getUserByFromUserName(String fromUserName) {
		return userService.getUserByFromUserName(fromUserName);
	}

	@Override
	public List<User> getAllValidUser() {
		return userService.getAllValidUser();
	}

	@Override
	public void createUser(String fromUserName) {
		userService.createUser(fromUserName);
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
