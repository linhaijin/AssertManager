package com.example.assertmanager.view;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assertmanager.adapter.SimpleAdapter;
import com.example.assertmanager.adapter.TreeViewadapter.OnTreeNodeClickListener;
import com.example.assertmanager.application.MyApplication;
import com.example.assertmanager.db.DBManager;
import com.example.assertmanager.model.BmBean;
import com.example.assertmanager.model.KmBean;
import com.example.assertmanager.util.Node;
import com.example.myqr_codescan.R;

public class SelectionBar extends LinearLayout implements OnClickListener {

	private static final int KM_QUERY_COMPLETED = 0;// 科目查詢結束
	private static final int BM_QUERY_COMPLETED = 1;// 部門查詢結束
	private static final int QUERY_ERROR = 2;// 查询发生错误
	private static final int ZERO_RESULT = 3;// 查询结果为空

	/**
	 * 接口
	 */
	public interface OnSelected {

		public void onAllSelect();// 选中了“全部”

		public void onKmSelect(String kmmc);// 选中了“科目”

		public void onBmSelect(String bmmc);// 选中了“部门”

		public void onFlagSelect(View v);// 选中了"清查"或者“报废”，留给用户实现

	}

	private OnSelected listener;

	public void setSelectedListener(OnSelected listener) {
		this.listener = listener;
	}

	private TextView allTv;// 全部文本框
	private TextView kmTv;// 科目文本框
	private TextView bmTv;// 部门文本框
	private TextView changeTv;// 可变的文本，清查时为清查，检索时为报废

	private boolean isCheck;// 清查标志

	private String normarlColor = "#929293";// 文本正常时的显示颜色

	private String selectColor = "#B97A28";// 本文选中时的显示颜色

	private ProgressDialog dialog;

	private List<KmBean> kmDatas;

	private List<BmBean> bmDatas;

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			closeDialog();
			switch (msg.what) {
			case KM_QUERY_COMPLETED:
				try {

					View rootView = LayoutInflater.from(getContext()).inflate(
							R.layout.mutiplt_lv_layout, null);
					ListView kmListView = (ListView) rootView
							.findViewById(R.id.mutiple_lv);
					SimpleAdapter<KmBean> kmAdapter = new SimpleAdapter<KmBean>(
							kmListView, getContext(), kmDatas, 2);
					kmListView.setAdapter(kmAdapter);
					final PopupWindow popupWindow = new PopupWindow(rootView,
							MyApplication.popWindowWidth,
							MyApplication.popWindowHeight);
					popupWindow.setFocusable(true);
					popupWindow.setBackgroundDrawable(new BitmapDrawable());
					popupWindow.setOutsideTouchable(true);
					popupWindow.showAsDropDown(kmTv);

					kmAdapter
							.setOnTreeNodeClickListeener(new OnTreeNodeClickListener() {

								@Override
								public void onClick(Node node, int position,
										int type) {

									if (type == 0 && node.isLeaf()) {
										String content = node.getName();
										if (listener != null) {
											listener.onKmSelect(content);
										}
										popupWindow.dismiss();
									}

									if (type == 1 && !node.isLeaf()) {

										String content = node.getName();
										if (listener != null) {
											listener.onKmSelect(content);
										}
										popupWindow.dismiss();
									}

								}
							});

				} catch (IllegalAccessException e) {

					Toast.makeText(getContext(), "显示发生错误", Toast.LENGTH_SHORT)
							.show();

				}

				break;
			case BM_QUERY_COMPLETED:

				try {

					View rootView = LayoutInflater.from(getContext()).inflate(
							R.layout.mutiplt_lv_layout, null);
					ListView bmListView = (ListView) rootView
							.findViewById(R.id.mutiple_lv);
					SimpleAdapter<BmBean> bmAdapter = new SimpleAdapter<BmBean>(
							bmListView, getContext(), bmDatas, 2);
					bmListView.setAdapter(bmAdapter);
					final PopupWindow popupWindow = new PopupWindow(rootView,
							MyApplication.popWindowWidth, MyApplication.popWindowHeight);
					popupWindow.setFocusable(true);
					popupWindow.setBackgroundDrawable(new BitmapDrawable());
					popupWindow.setOutsideTouchable(true);
					popupWindow.update();
					popupWindow.showAsDropDown(kmTv);
					bmAdapter
							.setOnTreeNodeClickListeener(new OnTreeNodeClickListener() {

								@Override
								public void onClick(Node node, int position,
										int type) {

									if (type == 0 && node.isLeaf()) {
										String content = node.getName();
										if (listener != null) {
											listener.onBmSelect(content);
										}
										popupWindow.dismiss();
									}

									if (type == 1 && !node.isLeaf()) {

										String content = node.getName();
										if (listener != null) {
											listener.onBmSelect(content);
										}
										popupWindow.dismiss();

									}

								}
							});

				} catch (IllegalAccessException e) {

					Toast.makeText(getContext(), "显示发生错误", Toast.LENGTH_SHORT)
							.show();

				}

				break;

			case QUERY_ERROR:

				Toast.makeText(getContext(), "查询结果错误", Toast.LENGTH_SHORT)
						.show();

				break;
			case ZERO_RESULT:

				Toast.makeText(getContext(), "无子项目选择", Toast.LENGTH_SHORT)
						.show();

				break;
			default:
				break;
			}

		};

	};

	public SelectionBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.SelectionBar);

		// 获取当前是清查还是检索
		int flag = ta.getInt(R.styleable.SelectionBar_flag, 0);
		isCheck = flag == 0 ? true : false;
		ta.recycle();

		setBackgroundColor(Color.parseColor("#16161B"));
		setGravity(Gravity.CENTER);
		setWeightSum(4);

		allTv = new TextView(context);
		kmTv = new TextView(context);
		bmTv = new TextView(context);
		changeTv = new TextView(context);

		locateAll();
		setText();
		setColor();
		select(0);

		setTags();
		setListeners();

		dialog = new ProgressDialog(context);
	}

	private String kmmc;// 查询时所用科目名称
	private String bmmc;// 查询时所用部门名称

	/*
	 * 设置初始的科目和部门查询，均在获取控件后设置 一旦设置，就不会再设置
	 */

	public void setKmmc(String kmmc) {
		this.kmmc = kmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	/**
	 * 布局四个文本框
	 */
	private void locateAll() {

		LinearLayout.LayoutParams lp = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		lp.gravity = Gravity.CENTER;

		allTv.setGravity(Gravity.CENTER_HORIZONTAL);
		kmTv.setGravity(Gravity.CENTER_HORIZONTAL);
		bmTv.setGravity(Gravity.CENTER_HORIZONTAL);
		changeTv.setGravity(Gravity.CENTER_HORIZONTAL);

		addView(allTv, lp);
		addView(kmTv, lp);
		addView(bmTv, lp);
		addView(changeTv, lp);

	}

	/*
	 * 设置文本
	 */
	private void setText() {
		allTv.setText("全部");
		kmTv.setText("科目");
		bmTv.setText("部门");
		if (isCheck)
			changeTv.setText("清查");
		else
			changeTv.setText("报废");
	}

	/*
	 * 设置颜色为普通颜色
	 */
	private void setColor() {

		allTv.setTextColor(Color.parseColor(normarlColor));
		kmTv.setTextColor(Color.parseColor(normarlColor));
		bmTv.setTextColor(Color.parseColor(normarlColor));
		changeTv.setTextColor(Color.parseColor(normarlColor));
	}

	/**
	 * 设置选中文本的颜色
	 * 
	 * @param index
	 */
	private void select(int index) {

		switch (index) {
		case 0:
			allTv.setTextColor(Color.parseColor(selectColor));
			break;
		case 1:
			kmTv.setTextColor(Color.parseColor(selectColor));
			break;
		case 2:
			bmTv.setTextColor(Color.parseColor(selectColor));
			break;
		case 3:
			changeTv.setTextColor(Color.parseColor(selectColor));
			break;
		default:
			break;
		}

	}

	/**
	 * 为四个文本框设置Tag,以序号作为其Tag
	 */
	private void setTags() {

		allTv.setTag(0);
		kmTv.setTag(1);
		bmTv.setTag(2);
		changeTv.setTag(3);
	}

	/**
	 * 设置监听器
	 */
	private void setListeners() {
		allTv.setOnClickListener(this);
		kmTv.setOnClickListener(this);
		bmTv.setOnClickListener(this);
		changeTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		setColor();// 清空颜色
		switch ((Integer) v.getTag()) {
		case 0:
			select(0);
			if (listener != null)
				listener.onAllSelect();
			break;
		case 1:
			select(1);

			showKm();

			break;
		case 2:
			select(2);

			showBm();

			break;
		case 3:

			select(3);
			if (listener != null) {
				listener.onFlagSelect(v);
			}

			break;
		default:
			break;
		}
	}

	private void showDialog() {
		dialog.setMessage("正在查询中...");
		dialog.show();
	}

	private void closeDialog() {
		if (dialog != null)
			dialog.dismiss();
	}

	/**
	 * 显示科目列表，如果之前已经做过查询，则直接显示，否则查询数据库
	 */
	private void showKm() {

		if (kmDatas != null && kmDatas.size() > 0) {
			mHandler.sendEmptyMessage(KM_QUERY_COMPLETED);
		} else if (kmDatas != null && kmDatas.size() == 0) {
			mHandler.sendEmptyMessage(ZERO_RESULT);
		} else {

			showDialog();

			new Thread(new Runnable() {

				@Override
				public void run() {
					kmDatas = DBManager.getInstance().getKmList(kmmc);

					if (kmDatas == null) {
						mHandler.sendEmptyMessage(QUERY_ERROR);
					} else {
						if (kmDatas.size() == 0) {
							mHandler.sendEmptyMessage(ZERO_RESULT);
						} else {
							mHandler.sendEmptyMessage(KM_QUERY_COMPLETED);
						}
					}

				}
			}).start();

		}
	}

	/**
	 * 显示部門列表，如果之前已经做过查询，则直接显示，否则查询数据库
	 */
	private void showBm() {
		if (bmDatas != null && bmDatas.size() > 0) {
			mHandler.sendEmptyMessage(BM_QUERY_COMPLETED);
		} else if (bmDatas != null && bmDatas.size() == 0) {
			mHandler.sendEmptyMessage(ZERO_RESULT);
		} else {

			showDialog();

			new Thread(new Runnable() {

				@Override
				public void run() {
					bmDatas = DBManager.getInstance().getBmList(bmmc);

					if (bmDatas == null) {
						mHandler.sendEmptyMessage(QUERY_ERROR);
					} else {
						if (bmDatas.size() == 0) {
							mHandler.sendEmptyMessage(ZERO_RESULT);
						} else {
							mHandler.sendEmptyMessage(BM_QUERY_COMPLETED);
						}
					}

				}
			}).start();

		}
	}
}
