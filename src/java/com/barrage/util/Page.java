package com.barrage.util;

import java.util.List;

public class Page {

	private long totalProperty;

	@SuppressWarnings("rawtypes")
	private List root;

	private int start;

	private int limit;

	private String conditions;

	public long getTotalProperty() {
		return totalProperty;
	}

	public void setTotalProperty(long totalProperty) {
		this.totalProperty = totalProperty;
	}

	@SuppressWarnings("rawtypes")
	public List getRoot() {
		return root;
	}

	@SuppressWarnings("rawtypes")
	public void setRoot(List root) {
		this.root = root;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

}
