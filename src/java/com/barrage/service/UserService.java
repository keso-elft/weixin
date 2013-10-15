package com.barrage.service;

import java.util.List;

import com.barrage.model.User;

public interface UserService {

	public User getUserByFromUserName(String fromUserName);

	public List<User> getAllValidUser();

	public void createUser(String fromUserName);

	public void saveUser(User user);

}
