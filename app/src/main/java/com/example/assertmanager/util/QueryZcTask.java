package com.example.assertmanager.util;

import java.util.List;

import com.example.assertmanager.db.DBManager;
import com.example.assertmanager.model.ZcBean;

/**
 * 查询资产的任务
 * 
 * @author Xingfeng
 *
 */
public class QueryZcTask implements Runnable {

	private String glbm;
	private String assertType;
	private String mxsybm;
	private int checkFlag;

	public QueryZcTask(String glbm, String assertType, String mxsybm,
			int checkFlag) {
		super();
		this.glbm = glbm;
		this.assertType = assertType;
		this.mxsybm = mxsybm;
		this.checkFlag = checkFlag;
	}
	
	

	public QueryZcTask(String mxsybm) {
		super();
		this.mxsybm = mxsybm;
	}



	public interface OnQueryCallbackListener {
	
		public void onSuccess(List<ZcBean> results);

		public void onError(Exception e);
	}

	private OnQueryCallbackListener listener;

	public void setOnQueryCallbackListener(OnQueryCallbackListener listener) {
		this.listener = listener;
	}

	@Override
	public void run() {

		List<ZcBean> results = DBManager.getInstance().query(glbm, assertType,
				mxsybm, checkFlag);

		if (results != null && listener != null) {
			listener.onSuccess(results);
		}

		if (results == null && listener != null) {

			listener.onError(new NullPointerException());
		}

	}

}
