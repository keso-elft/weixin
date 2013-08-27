package com.barrage.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PageList 接口的实现，此类主要用于分页显示数据，只能从PageManager来取得！ Creation date:(2002-12-4 14:27:00)
 * 
 * @author：redbeans
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PageList extends AbstractList {

	private static final int DEFAULT_PAGE_SIZE = 15;

	private boolean init = false;

	private List list = new ArrayList(DEFAULT_PAGE_SIZE);

	private int pageCount;

	private int pageIndex = 1;

	private int pageSize = DEFAULT_PAGE_SIZE;

	private int recordCount;

	private int startRow = 0;

	/**
	 * 
	 * @param c
	 * @return
	 */
	public boolean setList(List c) {
		list.clear();
		return list.addAll(c);
	}

	/**
	 * 
	 */
	@Override
	public Object get(int index) {
		return list.get(index);
	}

	/**
	 * @return Returns the pageCount.
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * @return Returns the pageNo.
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * @return Returns the pageSize.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return Returns the recordCount.
	 */
	public int getRecordCount() {
		return recordCount;
	}

	protected int getStartRow() {
		if (!init) {
			initialize();
		}
		return startRow;
	}

	/**
	 * 
	 * 
	 */
	public void initialize() {
		if (recordCount % pageSize == 0) {
			pageCount = recordCount / pageSize;
		} else {
			pageCount = recordCount / pageSize + 1;
		}
		if (pageIndex > pageCount) {
			pageIndex = pageCount;
			startRow = (pageIndex - 1) * pageSize;
		} else if (pageIndex < 1) {
			pageIndex = 0;
			startRow = 0;
		}
		init = true;
	}

	@Override
	public Iterator iterator() {
		return list.iterator();
	}

	public void setPageIndex(int pageNumber) {
		pageIndex = pageNumber;
	}

	public void setPageSize(int newPageSize) {
		if (newPageSize < 1) {
			pageSize = 10;
		} else {
			pageSize = newPageSize;
		}
	}

	public void setRecordCount(int newRecordCount) {
		recordCount = newRecordCount;
	}

	@Override
	public int size() {
		return list.size();
	}

	/**
	 * @return Returns the list.
	 */
	public List getList() {
		return list;
	}
}
