package com.example.assertmanager.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 改变的资产bean
 * 
 * @author Xingfeng
 *
 */
public class ChangeZcBean {

	/**
	 * 资产编号
	 */
	private String mxbh;

	/**
	 * 清查报废标志
	 */
	private int qcbfbz;

	/**
	 * 清查调整标志
	 */
	private int qctzbz;

	/**
	 * 清查条码标志
	 */
	private int qctmbz;

	/**
	 * 清查图片标志
	 */
	private int qctpbz;

	/**
	 * 清查数量（清查标志）
	 */
	private int qcsl;

	public void setMxbh(String mxbh) {
		this.mxbh = mxbh;
	}

	public void setQcbfbz(int qcbfbz) {
		this.qcbfbz = qcbfbz;
	}

	public void setQctzbz(int qctzbz) {
		this.qctzbz = qctzbz;
	}

	public void setQctmbz(int qctmbz) {
		this.qctmbz = qctmbz;
	}

	public void setQctpbz(int qctpbz) {
		this.qctpbz = qctpbz;
	}

	public void setQcsl(int qcsl) {
		this.qcsl = qcsl;
	}

	
	/**
	 * 加上时间
	 */
	@Override
	public String toString() {
		return formatTime() + " [mxbh=" + mxbh + ", qcbfbz=" + qcbfbz
				+ ", qctzbz=" + qctzbz + ", qctmbz=" + qctmbz + ", qctpbz="
				+ qctpbz + ", qcsl=" + qcsl + "]";
	}

	private String formatTime() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.format(new Date());

	}

}
