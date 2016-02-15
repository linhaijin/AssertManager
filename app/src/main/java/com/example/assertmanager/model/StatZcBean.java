package com.example.assertmanager.model;

/**
 * 统计资产条目，用于分组实现
 * 
 * @author Xingfeng
 *
 */
public class StatZcBean extends ZcBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1995726292109080397L;

	/**
	 * 该分组下的数目
	 */
	private int size;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
