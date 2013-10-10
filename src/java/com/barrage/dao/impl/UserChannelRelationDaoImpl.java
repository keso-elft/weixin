package com.barrage.dao.impl;

import java.util.List;

import com.barrage.dao.UserChannelRelationDao;
import com.barrage.model.UserChannelRelation;

public class UserChannelRelationDaoImpl extends SuperDao implements UserChannelRelationDao {

	public void join(String fromUserName, long channelId) {

		String hql = "from UserChannelRelation where fromUserName = ? and channelId = ?";
		@SuppressWarnings("unchecked")
		List<UserChannelRelation> list = getHibernateTemplate().find(hql, new Object[] { fromUserName, channelId });

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

	public void quit(String fromUserName) {
		@SuppressWarnings("unchecked")
		List<UserChannelRelation> list = getHibernateTemplate().find(
				"from UserChannelRelation where fromUserName = ? and status = 0", fromUserName);

		UserChannelRelation userChannelRelation;

		if (list != null && list.size() > 0) {
			userChannelRelation = list.get(0);
			userChannelRelation.setStatus(1l);

			getHibernateTemplate().saveOrUpdate(userChannelRelation);
		}
	}

	@SuppressWarnings("unchecked")
	public Long getJoinChannelId(String fromUserName) {
		List<UserChannelRelation> result = getHibernateTemplate().find(
				"from UserChannelRelation where fromUserName = ? and status = 0", fromUserName);
		if (!result.isEmpty())
			return ((UserChannelRelation) result.get(0)).getId();
		return null;
	}

}
