package com.barrage.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.barrage.dao.UserChannelRelationDao;
import com.barrage.model.UserChannelRelation;

public class UserChannelRelationHibernate extends EntityDaoHibernate<UserChannelRelation, Serializable> implements
		UserChannelRelationDao {

	@Override
	public void join(String fromUserName, long channelId) {
		List<Object> params = new ArrayList<Object>();
		params.add(fromUserName);
		params.add(channelId);
		@SuppressWarnings("unchecked")
		List<UserChannelRelation> list = getHibernateTemplate().find(
				"from UserChannelRelation where fromUserName = ? and channelId = ? and status = 0", params);

		UserChannelRelation userChannelRelation;

		if (list != null && list.size() > 0) {
			userChannelRelation = list.get(0);
		} else {
			userChannelRelation = new UserChannelRelation();
		}
		userChannelRelation.setChannelId(channelId);
		userChannelRelation.setFromUserName(fromUserName);
		userChannelRelation.setStatus(0l);

		getHibernateTemplate().saveOrUpdate(userChannelRelation);
	}

	@Override
	public void quit(String fromUserName) {
		List<Object> params = new ArrayList<Object>();
		params.add(fromUserName);
		@SuppressWarnings("unchecked")
		List<UserChannelRelation> list = getHibernateTemplate().find(
				"from UserChannelRelation where fromUserName = ? and status = 0", params);

		UserChannelRelation userChannelRelation;

		if (list != null && list.size() > 0) {
			userChannelRelation = list.get(0);
			userChannelRelation.setStatus(1l);

			getHibernateTemplate().saveOrUpdate(userChannelRelation);
		}
	}

	@Override
	public UserChannelRelation getRelationsByFromUserName(String fromUserName) {
		@SuppressWarnings("unchecked")
		List<UserChannelRelation> list = getHibernateTemplate().find(
				"from UserChannelRelation where fromUserName = ? and status = 0", fromUserName);

		UserChannelRelation userChannelRelation = null;
		if (list != null && list.size() > 0) {
			userChannelRelation = list.get(0);
		}
		return userChannelRelation;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserChannelRelation> getRelationsByFromChannelId(Long channelId) {
		return getHibernateTemplate().find("from UserChannelRelation where channelId = ? and status = 0", channelId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserChannelRelation> getAllValidRelation() {
		return getHibernateTemplate().find("from UserChannelRelation where status = 0");
	}

}
