package com.example.assertmanager.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.assertmanager.adapter.SimpleAdapter;
import com.example.assertmanager.adapter.TreeViewadapter.OnTreeNodeClickListener;
import com.example.assertmanager.application.MyApplication;
import com.example.assertmanager.db.DBManager;
import com.example.assertmanager.model.BmBean;
import com.example.assertmanager.model.KmBean;
import com.example.assertmanager.util.Node;
import com.example.myqr_codescan.R;

public class MutipleSpnnier<T> extends FrameLayout implements
		View.OnClickListener {

	private RelativeLayout parentRl;
	private TextView contentTv;

	public RelativeLayout getParentRl() {
		return parentRl;
	}

	public TextView getContentTv() {
		return contentTv;
	}

	private PopupWindow popupWindow;

	private List<T> mDatas;

	private SimpleAdapter<T> adapter;

	public MutipleSpnnier(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.my_spnnier_layout, this);
		parentRl = (RelativeLayout) view.findViewById(R.id.parent_rl);
		contentTv = (TextView) view.findViewById(R.id.content_tv);
		parentRl.setOnClickListener(this);

		rootView = LayoutInflater.from(getContext()).inflate(
				R.layout.mutiplt_lv_layout, null);
		listView = (ListView) rootView.findViewById(R.id.mutiple_lv);
	}

	private T type;

	public void setType(T t) {

		type = t;

	}

	public void setDatas(List<T> mDatas) {

		this.mDatas = (List<T>) mDatas;

	}

	private OnRlClickListener rlListener;

	public void setOnRlClickListener(OnRlClickListener listener) {

		this.rlListener = listener;

	}

	public interface OnRlClickListener {

		public void onClcik();

	}

	private ProgressDialog progressDialog;

	private ListView listView;

	private View rootView;

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			dismissDialog();

			switch (msg.what) {
			case 0:

				adapter.setOnTreeNodeClickListeener(new OnTreeNodeClickListener() {

					@Override
					public void onClick(Node node, int position, int type) {

						// 叶子节点
						if (type == 0 && node.isLeaf()) {
							setTitle(node.getName());
							popupWindow.dismiss();

							if (listener != null)
								listener.onItemClick(node.getName());
						}

						// 非叶子节点
						if (type == 1 && !node.isLeaf()) {

							setTitle(node.getName());
							popupWindow.dismiss();

							if (listener != null)
								listener.onItemClick(node.getName());

						}

					}
				});

				popupWindow = new PopupWindow(rootView,
						(int)(MyApplication.popWindowWidth*3/2),
						MyApplication.popWindowHeight);
				popupWindow.setFocusable(true);
				popupWindow.setBackgroundDrawable(new BitmapDrawable());
				popupWindow.setOutsideTouchable(true);
				// popupWindow.setAnimationStyle(R.style.popupwindow_animation);
				popupWindow.showAsDropDown(parentRl);

				break;

			default:
				break;
			}

		};

	};

	private void showDialog() {

		if (progressDialog == null)
			progressDialog = new ProgressDialog(getContext());

		progressDialog.setMessage("正在整理数据中...");
		progressDialog.show();

	}

	private void dismissDialog() {

		if (progressDialog != null)
			progressDialog.dismiss();

	}

	List<KmBean> kmList;

	List<BmBean> bmList;

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {

		showDialog();

		new Thread(new Runnable() {

			@Override
			public void run() {

				if (type instanceof KmBean) {
					kmList = DBManager.getInstance().getKmList(queryMc);
					try {
						adapter = new SimpleAdapter(listView, getContext(),
								kmList, expandNum);
						listView.setAdapter(adapter);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				if (type instanceof BmBean) {
					bmList = DBManager.getInstance().getBmList(queryMc);
					try {
						adapter = new SimpleAdapter(listView, getContext(),
								bmList, expandNum);
						listView.setAdapter(adapter);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				mHandler.sendEmptyMessage(0);
			}
		}).start();

	}

	// 查询条件
	private String queryMc = "";
	// 展开级数
	private int expandNum = 1;

	/**
	 * 设置展开显示时的样式以及查询条件
	 * 
	 * @param parentMc
	 *            查询时的父名称
	 * @param expandNum
	 *            展开级数
	 */
	public void setDispaly(String parentMc, int expandNum) {

		this.queryMc = parentMc;
		this.expandNum = expandNum;

	}

	/**
	 * 设置标题文字
	 * 
	 * @param title
	 */
	public void setTitle(CharSequence title) {
		contentTv.setText(title);
	}

	/**
	 * 获取标题文字
	 * 
	 * @return
	 */
	public CharSequence getTitle() {

		return contentTv.getText();

	}

	public interface OnSpinnerItemClickListener {
		/**
		 * 文本
		 * 
		 * @param name
		 */
		public void onItemClick(String name);

	}

	private OnSpinnerItemClickListener listener;

	public void setListener(OnSpinnerItemClickListener listener) {
		this.listener = listener;
	}

}
