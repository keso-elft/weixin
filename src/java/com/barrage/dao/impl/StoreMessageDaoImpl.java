package com.barrage.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.barrage.dao.StoreMessageDao;
import com.barrage.model.StoreMessage;

public class StoreMessageDaoImpl extends SuperDao implements StoreMessageDao {

	@Override
	public void saveMessage(Long userId, Long channelId, String content) {
		StoreMessage msg = new StoreMessage();
		msg.setUserId(userId);
		msg.setChannelId(channelId);
		msg.setContent(content);
		msg.setSendTime(new Date());

		getHibernateTemplate().save(msg);

	}

	@Override
	public void saveMessages(final List<StoreMessage> list) {

		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				String sql = "insert into store_message (user_id,channel_id,content,send_time) values(?,?,?,now())";

				@SuppressWarnings("deprecation")
				Connection conn = session.connection();
				PreparedStatement pst = null;
				pst = conn.prepareStatement(sql);

				if (list != null && !list.isEmpty()) {
					for (StoreMessage msg : list) {
						pst.setLong(1, msg.getUserId());
						pst.setLong(2, msg.getChannelId());
						pst.setString(3, msg.getContent());
						pst.addBatch();
					}
					pst.executeBatch();
				}
				session.flush();
				session.clear();
				return null;
			}
		});
	}
}
