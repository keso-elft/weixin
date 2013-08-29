package com.barrage.dao.hibernate;

import java.io.Serializable;

import com.barrage.dao.StoreMessageDao;
import com.barrage.model.StoreMessage;

public class StoreMessageHibernate extends EntityDaoHibernate<StoreMessage, Serializable> implements StoreMessageDao {

}
