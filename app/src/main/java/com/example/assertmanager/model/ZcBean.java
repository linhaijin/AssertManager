package com.example.assertmanager.model;

import java.io.Serializable;

/**
 * 资产Bean
 * 
 * @author Xingfeng
 *
 */
public class ZcBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8731375485063204822L;

	/**
	 * 对应图片名称
	 */
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 明细编号，主键，由mxid+18位组成
	 */
	private String mxbh;

	/**
	 * 科目
	 */
	private String km;

	/**
	 * 明细数量
	 */
	private int mxsl;

	/**
	 * 明细现管理部门
	 */
	private String mxxglbm;

	/**
	 * 保管人
	 */
	private String mxbgr;

	/**
	 * 明细使用年限
	 */
	private int mxsynx;

	/**
	 * 使用状况
	 */
	private int mxsyzk;

	/**
	 * 报废数量
	 */
	private int mxjssl;

	public int getMxsyzk() {
		return mxsyzk;
	}

	public void setMxsyzk(int mxsyzk) {
		this.mxsyzk = mxsyzk;
	}

	/**
	 * 明细报废金额
	 */
	private float mxjsje;

	/**
	 * 明细金额
	 */
	private float mxje;

	/**
	 * 名称
	 */
	private String mc;

	/**
	 * 日期yyyy/m/d
	 */
	private String rq;

	/**
	 * 使用部门
	 */
	private String mxsybm;

	/**
	 * 规格型号
	 */
	private String ggxh;

	/**
	 * 序号
	 */
	private int xh;

	/**
	 * 单价
	 */
	private int dj;

	/**
	 * 通知单号
	 */
	private int tzdh;

	/**
	 * 摘要
	 */
	private String zy;

	/**
	 * 入账标志
	 */
	private int rzbz;

	/**
	 * 清查报废标志
	 */
	private int qcbfbz;

	/**
	 * 清查调整标志
	 */
	private int qctzbz;

	/**
	 * 清查条码标识
	 */
	private int qctmbz;

	/**
	 * 清查图片标识
	 */
	private int qctpbz;

	/**
	 * 清查日期
	 */
	private String qcrq;

	/**
	 * 数量
	 */
	private int sl;
	
	public int getSl() {
		return sl;
	}

	public void setSl(int sl) {
		this.sl = sl;
	}

	public String getQcrq() {
		return qcrq;
	}

	public void setQcrq(String qcrq) {
		this.qcrq = qcrq;
	}

	public int getQcbfbz() {
		return qcbfbz;
	}

	public void setQcbfbz(int qcbfbz) {
		this.qcbfbz = qcbfbz;
	}

	public int getQctzbz() {
		return qctzbz;
	}

	public void setQctzbz(int qctzbz) {
		this.qctzbz = qctzbz;
	}

	public int getQctmbz() {
		return qctmbz;
	}

	public void setQctmbz(int qctmbz) {
		this.qctmbz = qctmbz;
	}

	public int getQctpbz() {
		return qctpbz;
	}

	public void setQctpbz(int qctpbz) {
		this.qctpbz = qctpbz;
	}

	public int getXh() {
		return xh;
	}

	public void setXh(int xh) {
		this.xh = xh;
	}

	public int getDj() {
		return dj;
	}

	public void setDj(int dj) {
		this.dj = dj;
	}

	public int getTzdh() {
		return tzdh;
	}

	public void setTzdh(int tzdh) {
		this.tzdh = tzdh;
	}

	public String getZy() {
		return zy;
	}

	public void setZy(String zy) {
		this.zy = zy;
	}

	public int getRzbz() {
		return rzbz;
	}

	public void setRzbz(int rzbz) {
		this.rzbz = rzbz;
	}

	public String getGgxh() {
		return ggxh;
	}

	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}

	/**
	 * 默认为0，识别后为1
	 */
	private int qcsl;

	public String getMxbh() {
		return mxbh;
	}

	public void setMxbh(String mxbh) {
		this.mxbh = mxbh;
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public int getQcsl() {
		return qcsl;
	}

	public void setQcsl(int qcsl) {
		this.qcsl = qcsl;
	}

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public String getMxsybm() {
		return mxsybm;
	}

	public void setMxsybm(String mxsybm) {
		this.mxsybm = mxsybm;
	}

	public String getKm() {
		return km;
	}

	public void setKm(String km) {
		this.km = km;
	}

	public int getMxsl() {
		return mxsl;
	}

	public void setMxsl(int mxsl) {
		this.mxsl = mxsl;
	}

	public String getMxxglbm() {
		return mxxglbm;
	}

	public void setMxxglbm(String mxxglbm) {
		this.mxxglbm = mxxglbm;
	}

	public String getMxbgr() {
		return mxbgr;
	}

	public void setMxbgr(String mxbgr) {
		this.mxbgr = mxbgr;
	}

	public int getMxsynx() {
		return mxsynx;
	}

	public void setMxsynx(int mxsynx) {
		this.mxsynx = mxsynx;
	}

	public int getMxjssl() {
		return mxjssl;
	}

	public void setMxjssl(int mxjssl) {
		this.mxjssl = mxjssl;
	}

	public float getMxjsje() {
		return mxjsje;
	}

	public void setMxjsje(float mxjsje) {
		this.mxjsje = mxjsje;
	}

	public float getMxje() {
		return mxje;
	}

	public void setMxje(float mxje) {
		this.mxje = mxje;
	}

}
