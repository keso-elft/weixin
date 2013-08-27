/*
 * EntityManagerImpl.java  Oct 13, 2008
 */
package com.barrage.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.barrage.dao.EntityDao;
import com.barrage.service.EntityManager;
import com.barrage.util.PageList;

/**
 * @author zhongyang
 */
public class EntityManagerImpl<T, ID extends Serializable> implements EntityManager<T, ID> {

	protected EntityDao<T, ID> entityDao;

	public ID save(T entity) {
		return entityDao.save(entity);
	}

	public void saveOrUpdate(T entity) {
		entityDao.saveOrUpdate(entity);
	}

	public void saveOrUpdateAll(List<T> entities) {
		entityDao.saveOrUpdateAll(entities);
	}

	public T get(ID id) {
		return entityDao.get(id);
	}

	public List<T> getByIds(ID[] ids) {
		List<T> list = new ArrayList<T>();
		for (ID id : ids) {
			list.add(get(id));
		}
		return list;
	}

	public List<T> getAll() {
		return entityDao.getAll();
	}

	public List<T> getListByCondition(Map<String, Object> conditions) {
		return entityDao.getListByCondition(conditions);
	}

	public void remove(T entity) {
		entityDao.remove(entity);
	}

	public void removeAll(List<T> entities) {
		entityDao.removeAll(entities);
	}

	public void removeAll() {
		entityDao.removeAll();
	}

	public void removeById(ID id) {
		entityDao.removeById(id);
	}

	public void removeByIds(ID[] ids) {
		entityDao.removeByIds(ids);
	}

	public PageList getPageList(int pageNo, int pageSize) {
		return entityDao.getPageList(pageNo, pageSize);
	}

	public PageList getPageListByCondition(Map<String, Object> conditions, int pageNo, int pageSize, String orderType,
			String orderField) {
		return entityDao.getPageListByCondition(conditions, pageNo, pageSize, orderType, orderField);
	}

}
