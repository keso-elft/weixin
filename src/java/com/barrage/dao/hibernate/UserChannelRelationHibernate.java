package com.barrage.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;

import com.barrage.dao.UserChannelRelationDao;
import com.barrage.model.UserChannelRelation;

public class UserChannelRelationHibernate extends EntityDaoHibernate<UserChannelRelation, Serializable> implements
		UserChannelRelationDao {

	@Override
	public void join(String fromUserName, long channelId) {

		String hql = "from UserChannelRelation where fromUserName = :fromUserName and channelId = :channelId";
		Query qry = getSession().createQuery(hql);
		qry.setString("fromUserName", fromUserName);
		qry.setLong("channelId", channelId);
		@SuppressWarnings("unchecked")
		List<UserChannelRelation> list = qry.list();

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
	public List<UserChannelRelation> getRelationsByChannelId(Long channelId) {
		return getHibernateTemplate().find("from UserChannelRelation where channelId = ? and status = 0", channelId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserChannelRelation> getAllValidRelation() {
		return getHibernateTemplate().find("from UserChannelRelation where status = 0");
	}

}
