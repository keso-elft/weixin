package com.barrage.dao.hibernate;

import java.io.Serializable;

import com.barrage.dao.UserDao;
import com.weixin.common.WeiXinFans;

public class UserHibernate extends EntityDaoHibernate<WeiXinFans, Serializable> implements UserDao {

}