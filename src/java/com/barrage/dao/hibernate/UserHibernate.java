package com.barrage.dao.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.barrage.dao.UserDao;
import com.weixin.common.WeiXinFans;

public class UserHibernate extends EntityDaoHibernate<WeiXinFans, Serializable> implements UserDao {

	@Override
	public WeiXinFans getUserByFromUserName(String fromUserName) {
		@SuppressWarnings("unchecked")
		List<WeiXinFans> list = getHibernateTemplate().find("from WeiXinFans where fromUserName = ?", fromUserName);

		WeiXinFans user = null;
		if (list != null && list.size() > 0) {
			user = list.get(0);
		}
		return user;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<WeiXinFans> getAllValidUser() {
		return getHibernateTemplate().find("from WeiXinFans");
	}

	@Override
	public void createUser(String fromUserName) {
		WeiXinFans user = new WeiXinFans();
		user.setFromUserName(fromUserName);
		user.setLastAccessedTime(new Date());
		save(user);
	}
}