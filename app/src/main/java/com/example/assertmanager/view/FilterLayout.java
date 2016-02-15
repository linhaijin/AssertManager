package com.example.assertmanager.view;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.assertmanager.adapter.SimpleAdapter;
import com.example.assertmanager.adapter.TreeViewadapter.OnTreeNodeClickListener;
import com.example.assertmanager.db.DBManager;
import com.example.assertmanager.model.CheckItemBean;
import com.example.assertmanager.model.KmBean;
import com.example.assertmanager.model.BmBean;
import com.example.assertmanager.util.DataSupport;
import com.example.assertmanager.util.Node;
import com.example.myqr_codescan.R;

public class FilterLayout<T> extends FrameLayout implements
		View.OnClickListener {

	private TextView allTv;

	private TextView kmTv;

	private TextView bmTv;

	private TextView flagTv;

	private String normarlColor = "#929293";

	private String selectColor = "#B97A28";

	private PopupWindow popupWindow;

	private ListView listView;

	private SimpleAdapter<T> adapter;

	private OnItemSelectedListener listener;

	public void setOnItemSelectedListener(OnItemSelectedListener listener) {

		this.listener = listener;

	}

	public interface OnItemSelectedListener {
		public void onItemSelected(int index, String content);
	}

	
	
	public FilterLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		View rootView = inflater.inflate(R.layout.filter_layout, this);

		allTv = (TextView) rootView.findViewById(R.id.all_tv);
		kmTv = (TextView) rootView.findViewById(R.id.km_tv);
		bmTv = (TextView) rootView.findViewById(R.id.bm_tv);
		flagTv = (TextView) rootView.findViewById(R.id.flag_tv);

		allTv.setOnClickListener(this);
		kmTv.setOnClickListener(this);
		bmTv.setOnClickListener(this);
		flagTv.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		resetTextViews();

		switch (v.getId()) {
		case R.id.all_tv:

			try {
				setSelected(0);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			break;

		case R.id.km_tv:

//			try {
//				setSelected(1);
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}

			break;

		case R.id.bm_tv:

//			try {
//				setSelected(2);
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}

			break;
		case R.id.flag_tv:

			try {
				setSelected(3);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			break;

		default:
			break;
		}

	}

	/**
	 * ListView的父容器
	 */
	private View rootView;

	/**
	 * 
	 * @param index
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("deprecation")
	private void setSelected(int index) throws IllegalAccessException {

		if (listView == null) {
			rootView = LayoutInflater.from(getContext()).inflate(
					R.layout.mutiplt_lv_layout, null);
			listView = (ListView) rootView.findViewById(R.id.mutiple_lv);
		}

		switch (index) {
		case 0:

			allTv.setTextColor(Color.parseColor(selectColor));
			if (listener != null) {
				listener.onItemSelected(0,
						getResources().getString(R.string.str_all));
			}

			break;

		case 1:

			if (popupWindow != null)
				popupWindow.dismiss();

			kmTv.setTextColor(Color.parseColor(selectColor));
			List<KmBean> kmDatas = DBManager.getInstance().getKmList("");
			adapter = new SimpleAdapter<T>(listView, getContext(), kmDatas, 1);
			listView.setAdapter(adapter);
			adapter.setOnTreeNodeClickListeener(new OnTreeNodeClickListener() {

				@Override
				public void onClick(Node node, int position, int type) {

					if (type == 0 && node.isLeaf()) {
						popupWindow.dismiss();

						if (listener != null)
							listener.onItemSelected(1, node.getName());
					}

					if (type == 1 && !node.isLeaf()) {

						popupWindow.dismiss();

						if (listener != null)
							listener.onItemSelected(1, node.getName());

					}

				}
			});
			popupWindow = new PopupWindow(rootView, 800, 800);
			popupWindow.setFocusable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setOutsideTouchable(true);
			popupWindow.showAsDropDown(kmTv);

			break;

		case 2:

			if (popupWindow != null)
				popupWindow.dismiss();

			bmTv.setTextColor(Color.parseColor(selectColor));
			List<BmBean> sybmDatas = DBManager.getInstance().getBmList("");
			adapter = new SimpleAdapter<T>(listView, getContext(), sybmDatas, 1);
			listView.setAdapter(adapter);
			adapter.setOnTreeNodeClickListeener(new OnTreeNodeClickListener() {

				@Override
				public void onClick(Node node, int position, int type) {

					if (type == 0 && node.isLeaf()) {
						popupWindow.dismiss();

						if (listener != null)
							listener.onItemSelected(3, node.getName());
					}

					if (type == 1 && !node.isLeaf()) {

						popupWindow.dismiss();

						if (listener != null)
							listener.onItemSelected(3, node.getName());

					}

				}
			});
			popupWindow = new PopupWindow(rootView, 500, 500);
			popupWindow.setFocusable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setOutsideTouchable(true);
			popupWindow.showAsDropDown(bmTv);

			break;

		case 3:

			if (popupWindow != null)
				popupWindow.dismiss();

			flagTv.setTextColor(Color.parseColor(selectColor));
			List<CheckItemBean> checkDatas = DataSupport.getCheckList();
			adapter = new SimpleAdapter<T>(listView, getContext(), checkDatas,
					1);
			listView.setAdapter(adapter);
			adapter.setOnTreeNodeClickListeener(new OnTreeNodeClickListener() {

				@Override
				public void onClick(Node node, int position, int type) {

					if (type == 0 && node.isLeaf()) {
						popupWindow.dismiss();

						if (listener != null)
							listener.onItemSelected(4, node.getName());
					}

					if (type == 1 && !node.isLeaf()) {

						popupWindow.dismiss();

						if (listener != null)
							listener.onItemSelected(4, node.getName());

					}

				}
			});
			popupWindow = new PopupWindow(rootView, 500, 300);
			popupWindow.setFocusable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setOutsideTouchable(true);
			popupWindow.showAsDropDown(flagTv);

			break;

		default:
			break;
		}

	}

	/**
	 * 灏嗘墍鏈夋枃瀛楅鑹插彉鎴愭櫘閫�
	 */
	private void resetTextViews() {

		allTv.setTextColor(Color.parseColor(normarlColor));
		kmTv.setTextColor(Color.parseColor(normarlColor));
		bmTv.setTextColor(Color.parseColor(normarlColor));
		flagTv.setTextColor(Color.parseColor(normarlColor));

	}

	/**
	 * 鏀瑰彉鏍囧織TextView鐨勬枃瀛�
	 * 
	 * @param text
	 */
	public void setFlagTvText(String text) {

		flagTv.setText(text);

	}
}
