package com.example.assertmanager.model;

import com.example.assertmanager.annotation.TreeNodeId;
import com.example.assertmanager.annotation.TreeNodeLabel;
import com.example.assertmanager.annotation.TreeNodePId;

/**
 * 清查Item
 * 
 * @author Xingfeng
 *
 */
public class CheckItemBean {

	@TreeNodeId
	private int id;

	@TreeNodePId
	private int pid;

	@TreeNodeLabel
	private String name;

	public CheckItemBean(int id, int pid, String name) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
