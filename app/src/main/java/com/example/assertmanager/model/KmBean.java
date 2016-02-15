package com.example.assertmanager.model;

import com.example.assertmanager.annotation.TreeNodeId;
import com.example.assertmanager.annotation.TreeNodeLabel;
import com.example.assertmanager.annotation.TreeNodePId;

public class KmBean {

	@TreeNodeId
	private int id;

	@TreeNodePId
	private int pid;

	@TreeNodeLabel
	private String name;

	/**
	 * 科目编码
	 */
	private String kmbm;
	
	
	public String getKmbm() {
		return kmbm;
	}

	public void setKmbm(String kmbm) {
		this.kmbm = kmbm;
	}

	public KmBean(int id, int pid, String name, String kmbm) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.kmbm = kmbm;
	}

	public KmBean(int id, int pid, String name) {
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
