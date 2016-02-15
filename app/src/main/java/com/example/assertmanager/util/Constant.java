package com.example.assertmanager.util;

import android.os.Environment;

public class Constant {

	/**
	 * 资产编号
	 */
	public final static String ZC_BH = "zc_bh";

	/**
	 * 资产名称
	 */
	public final static String ZC_MC = "zc_mc";

	/**
	 * 资产规格型号
	 */
	public final static String ZC_GGXH = "zc_ggxh";

	/**
	 * 资产保管人
	 */
	public final static String ZC_BGR = "zc_bgr";

	/**
	 * 管理部门
	 */
	public final static String ZC_GLBM = "zc_glbm";

	/**
	 * 资产科目
	 */
	public final static String ZC_KM = "zc_km";

	/**
	 * 使用部门
	 */
	public final static String ZC_SYBM = "zc_sybm";

	/**
	 * 摘要
	 */
	public final static String ZC_ZY = "zc_zy";

	/**
	 * 数据库文件名
	 */
	public final static String DB_FILE_NAME = "jdcwpt_zc.db";

	/**
	 * 资产Bean的key
	 */
	public final static String ZC_BEAN_KEY = "zc_bean_key";

	/**
	 * 应用文件存放路径
	 */
	public final static String FILE_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/AssertManager/";

	/**
	 * 图片存放路径
	 */
	public final static String PICTURE_PATH = FILE_PATH + "Picture/";

	/**
	 * 改变图片存放路径
	 */
	public final static String CHANGE_PICTURE_PATH = FILE_PATH
			+ "ChangePicture/";

	/**
	 * 日志文件存放路径
	 */
	public final static String LOG_PATH = FILE_PATH + "log/";

	public final static String DB_PATH = FILE_PATH + "db/";

	public final static String OUTPUT_PATH = FILE_PATH + "output/";

	/**
	 * 清查日期key
	 */
	public final static String QCRQ_KEY = "qcrq";

	public final static String UPDATE_QCRQ_ACTION = "com.example.uodata.qcrq.action";

	/**
	 * 图片改变标志
	 */
	public final static String PICTURE_CHANGE_FLAG = "picture_change_flag";

	/**
	 * 檢索
	 */
	public final static int RETRIEVE = 0;

	/**
	 * 清查
	 */
	public final static int CHECK = 1;

	/**
	 * 清查结果列表
	 */
	public final static String CHECK_RESULT_LIST = "check_result_list";

	/**
	 * 檢索結果列表
	 */
	public final static String RETRIEVE_RESULT_LIST = "retrieve_result_list";
}
