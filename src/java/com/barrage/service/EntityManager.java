/*
 * EntityManager.java  Oct 13, 2008
 */
package com.barrage.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.barrage.util.PageList;

/**
 * 针对Entity对象的servcie操作
 * @author zhongyang
 * @param <T>
 */
public interface EntityManager<T, ID extends Serializable> {

	/**
	 * 根据ID获取实例
	 * @param id
	 * @return
	 */
	public T get(ID id);

	/**
	 * 根据ID获取实例
	 * @param ids
	 * @return
	 */
	public List<T> getByIds(ID[] ids);

	/**
	 * 获取所有实例
	 * @return
	 */
	public List<T> getAll();

	/**
	 * 根据条件查询实例列表
	 * @param conditions
	 * @return
	 */
	public List<T> getListByCondition(Map<String, Object> conditions);

	/**
	 * 保存一个实例
	 * @param entity	entity to save
	 * @return	entity's identifier
	 */
	public ID save(T entity);

	/**
	 * 保存或更新一个实例
	 * @param entity	entity to save or update
	 */
	public void saveOrUpdate(T entity);

	/**
	 * 保存或更新一个实例列表
	 * @param entities	entities to save or update
	 */
	public void saveOrUpdateAll(List<T> entities);

	/**
	 * 根据ID删除实例
	 * @param id
	 */
	public void removeById(ID id);

	/**
	 * 根据ID删除实例
	 * @param ids
	 */
	public void removeByIds(ID[] ids);

	/**
	 * 删除一个实例
	 * @param entity
	 */
	public void remove(T entity);

	/**
	 * 删除一个实例列表
	 * @param entities
	 */
	public void removeAll(List<T> entities);

	/**
	 * 删除所有实例
	 */
	public void removeAll();

	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageList getPageList(int pageNo, int pageSize);

	/**
	 * 根据条件分页查询
	 * @param conditions
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageList getPageListByCondition(Map<String, Object> conditions, int pageNo, int pageSize, String orderType,
			String orderField);

}
