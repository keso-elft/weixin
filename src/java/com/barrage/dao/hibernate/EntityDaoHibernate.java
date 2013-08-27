/*
 * EntityDaoHibernate.java  Oct 13, 2008
 */
package com.barrage.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.barrage.dao.EntityDao;
import com.barrage.util.GenericsUtil;
import com.barrage.util.PageList;
import com.barrage.util.TimeUtil;

/**
 * EntityDao<T>的Hibernate实现
 * 继承于Spring的<code>HibernateDaoSupport</code>
 * 
 * @author zhongyang
 */
public class EntityDaoHibernate<T, ID extends Serializable> extends HibernateDaoSupport implements EntityDao<T, ID> {
	protected static Logger log = Logger.getLogger(EntityDaoHibernate.class);

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public EntityDaoHibernate() {
		// 构造时对泛型的类型进行初始化
		entityClass = GenericsUtil.getSuperClassGenricType(getClass());
	}

	/**
	 * 返回泛型的类型
	 * @return
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}

	@SuppressWarnings("unchecked")
	public T get(ID id) {
		return (T) getHibernateTemplate().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return getHibernateTemplate().loadAll(entityClass);
	}

	@SuppressWarnings("unchecked")
	public List<T> getListByCondition(Map<String, Object> conditions) {
		String tableName = getEntityClass().getSimpleName();

		StringBuffer queryString = new StringBuffer("from " + tableName);
		StringBuffer whereString = new StringBuffer(" where 1 = 1 ");
		List<Object> params = new ArrayList<Object>();

		setQueryParam(conditions, whereString, params);

		queryString.append(whereString.toString());

		return getHibernateTemplate().find(queryString.toString(), params.toArray());
	}

	@SuppressWarnings("unchecked")
	public ID save(T entity) {
		return (ID) getHibernateTemplate().save(entity);
	}

	public void saveOrUpdate(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
	}

	public void saveOrUpdateAll(List<T> entities) {
		getHibernateTemplate().saveOrUpdateAll(entities);
	}

	public void removeById(ID id) {
		getHibernateTemplate().bulkUpdate("delete from " + entityClass.getName() + " where id = ?", id);
	}

	public void removeByIds(ID[] ids) {
		for (ID id : ids) {
			removeById(id);
		}
	}

	public void remove(T entity) {
		getHibernateTemplate().delete(entity);
	}

	public void removeAll(List<T> entities) {
		getHibernateTemplate().deleteAll(entities);
	}

	public void removeAll() {
		getHibernateTemplate().bulkUpdate("delete from " + entityClass.getName());
	}

	public PageList getPageList(int pageNo, int pageSize) {
		String countString = "select count(*) from " + entityClass.getName();
		String queryString = "from " + entityClass.getName();
		return getPageList(countString, queryString, null, pageNo, pageSize);
	}

	protected PageList getPageList(String countString, String queryString, Object[] params, int pageNo, int pageSize) {
		PageList pageList = new PageList();
		@SuppressWarnings("rawtypes")
		List list = getHibernateTemplate().find(countString, params);
		if (list == null || list.isEmpty() || list.get(0) == null) {
			return pageList;
		}
		int recordCount = ((Long) list.get(0)).intValue();

		pageList.setPageIndex(pageNo);
		pageList.setRecordCount(recordCount);
		pageList.setPageSize(pageSize);
		pageList.initialize();
		if (recordCount == 0) {
			return pageList;
		}

		Query query = getSession().createQuery(queryString);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		query.setFirstResult((pageList.getPageIndex() - 1) * pageSize);
		query.setMaxResults(pageSize);

		pageList.setList(query.list());

		return pageList;
	}

	public PageList getPageListByCondition(Map<String, Object> conditions, int pageNo, int pageSize, String orderType,
			String orderField) {
		String tableName = getEntityClass().getSimpleName();

		StringBuffer countString = new StringBuffer("select count(id) from " + tableName);
		StringBuffer queryString = new StringBuffer("from " + tableName);
		StringBuffer whereString = new StringBuffer(" where 1 = 1 ");
		List<Object> params = new ArrayList<Object>();

		setQueryParam(conditions, whereString, params);

		countString.append(whereString.toString());
		queryString.append(whereString.toString());
		if (StringUtils.isNotBlank(orderField)) {
			if (!"desc".equalsIgnoreCase(orderType)) {
				orderType = "";
			}
			queryString.append(" order by " + orderField + " " + orderType);
		}

		return getPageList(countString.toString(), queryString.toString(), params.toArray(), pageNo, pageSize);
	}

	protected void setQueryParam(Map<String, Object> conditions, StringBuffer whereString, List<Object> params) {
		if (conditions == null) {
			return;
		}
		Set<Map.Entry<String, Object>> conditionSet = conditions.entrySet();
		for (Map.Entry<String, Object> condition : conditionSet) {
			String paramName = condition.getKey();
			Object paramValue = condition.getValue();
			if (StringUtils.isNotBlank(paramName) && paramValue != null) {
				if (paramValue instanceof String) {
					String value = paramValue.toString().trim();
					if (StringUtils.isNotBlank((String) paramValue)) {
						whereString.append(" and " + paramName + " like ? ");
						params.add("%" + value + "%");
					}
				} else if (paramValue instanceof Date) {
					if (paramName.endsWith("_beginTime")) {
						whereString.append(" and " + paramName.replaceAll("_beginTime", "") + " >= ?");
						params.add(TimeUtil.getBeginOfDay((Date) paramValue));
					} else if (paramName.endsWith("_endTime")) {
						whereString.append(" and " + paramName.replaceAll("_endTime", "") + " <= ?");
						params.add(TimeUtil.getEndOfDay((Date) paramValue));
					} else {
						whereString.append(" and " + paramName + " = ?");
						params.add(paramValue);
					}
				} else if (paramValue instanceof List<?>) {
					List<?> values = (List<?>) paramValue;
					if (!values.isEmpty()) {
						whereString.append(" and " + paramName.replace("List", "") + " in (");
						for (int i = 0; i < values.size(); i++) {
							whereString.append("?");
							if (i != values.size() - 1) {
								whereString.append(",");
							}
							params.add(values.get(i));
						}
						whereString.append(")");
					}
				} else {
					whereString.append(" and " + paramName + " = ?");
					params.add(paramValue);
				}
			}
		}
	}

}
