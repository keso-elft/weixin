package com.barrage.dao;

import java.util.List;

import com.barrage.model.User;

public interface UserDao {

	public User getUserByFromUserName(String fromUserName);

	public List<User> getAllValidUser();

	public void createUser(String fromUserName);

	public void saveUser(User user);

}
