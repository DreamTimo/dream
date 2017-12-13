package com.lykj.model;



import com.dlbase.base.DLBaseModel;

public class LYPageModel extends DLBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3395077308871929357L;

	private int nextMax;
	private int limit;
	private boolean hasMore;
	private boolean isRefresh = false;
	private boolean isLoaingMore = false;
	private String max ;
	
	public LYPageModel(int max, int limit, boolean hasMore){
		this.nextMax = max;
		this.limit = limit;
		this.hasMore = hasMore;
	}

	public int getNextMax() {
		return nextMax;
	}

	public void setNextMax(int nextMax) {
		this.nextMax = nextMax;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isRefresh() {
		return isRefresh;
	}

	public void setRefresh(boolean isRefresh) {
		this.isRefresh = isRefresh;
	}

	public boolean isLoaingMore() {
		return isLoaingMore;
	}

	public void setLoaingMore(boolean isLoaingMore) {
		this.isLoaingMore = isLoaingMore;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}
}
