package com.example.assertmanager.model;

public class Bean {

	private String mxid;
	
	private String mxxglbm;

	public String getMxid() {
		return mxid;
	}

	public void setMxid(String mxid) {
		this.mxid = mxid;
	}

	public String getMxxglbm() {
		return mxxglbm;
	}

	public void setMxxglbm(String mxxglbm) {
		this.mxxglbm = mxxglbm;
	}

	public Bean(String mxid, String mxxglbm) {
		super();
		this.mxid = mxid;
		this.mxxglbm = mxxglbm;
	}
	
	
}
