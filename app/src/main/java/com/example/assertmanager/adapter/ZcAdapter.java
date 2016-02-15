package com.example.assertmanager.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.assertmanager.model.StatZcBean;
import com.example.assertmanager.model.ZcBean;
import com.example.myqr_codescan.R;

public class ZcAdapter extends ArrayAdapter<ZcBean> {

	private LayoutInflater inflater;

	private List<ZcBean> datas;

	/**
	 * 报废标志颜色
	 */
	private String unDirtyColor = "#84B677";

	/**
	 * 未报废标志颜色
	 */
	private String dirtyColor = "#E3534B";

	public ZcAdapter(Context context, int resource, List<ZcBean> objects) {
		super(context, resource, objects);
		inflater = LayoutInflater.from(context);
		datas = objects;

	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {

			convertView = inflater.inflate(R.layout.zc_desc_item_layout, null);

			holder = new ViewHolder();
			holder.zcInfoRl = (RelativeLayout) convertView
					.findViewById(R.id.zc_info_rl);
			holder.zcMcTv = (TextView) convertView.findViewById(R.id.zc_mc);
			holder.zcCheckFlagTv = (TextView) convertView
					.findViewById(R.id.check_flag_tv);
			holder.zcGgxhTv = (TextView) convertView.findViewById(R.id.zc_ggxh);
			holder.zcRqTv = (TextView) convertView.findViewById(R.id.zc_rq);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ZcBean bean = datas.get(position);

		// 报废与否
		// 未报废
		if (bean.getQcbfbz() == 0) {
			holder.zcInfoRl.setBackgroundColor(Color.parseColor(unDirtyColor));
		} else {

			holder.zcInfoRl.setBackgroundColor(Color.parseColor(dirtyColor));

		}

		// 名称
		holder.zcMcTv.setText(bean.getMc());

		// 清查与否
		// 未清查
		if (bean.getQcsl() == 0) {

			holder.zcCheckFlagTv.setText("未清查");
			holder.zcCheckFlagTv.setTextColor(Color.BLACK);

		} else {

			holder.zcCheckFlagTv.setText("已清查");
			holder.zcCheckFlagTv.setTextColor(Color.WHITE);

		}

		// 规格型号
		holder.zcGgxhTv.setText(bean.getGgxh());

		// 日期
		holder.zcRqTv.setText(bean.getRq());

		return convertView;
	}

	@Override
	public int getItemViewType(int pos) {

		ZcBean bean = datas.get(pos);

		if (bean instanceof StatZcBean)
			return 1;
		else
			return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	class ViewHolder {

		/**
		 * 顶部的布局，显示资产名称以及清查标志
		 */
		RelativeLayout zcInfoRl;

		/**
		 * 资产名称
		 */
		TextView zcMcTv;

		/**
		 * 资产清查状态
		 */
		TextView zcCheckFlagTv;

		/**
		 * 资产规格型号
		 */
		TextView zcGgxhTv;

		/**
		 * 资产日期
		 */
		TextView zcRqTv;

	}
}
