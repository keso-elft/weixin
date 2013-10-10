package com.barrage.dao;

import java.util.List;

import com.weixin.common.User;

public interface UserDao {

	public User getUserByFromUserName(String fromUserName);

	public List<User> getAllValidUser();

	public void createUser(String fromUserName);

}
