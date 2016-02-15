package com.example.assertmanager.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.assertmanager.util.Node;
import com.example.myqr_codescan.R;

/**
 * Created by Xingfeng on 2015/6/8.
 */
public class SimpleAdapter<T> extends TreeViewadapter<T> {
	
	@SuppressWarnings("hiding")
	public <T> SimpleAdapter(ListView mTree, Context context, List<T> datas,
			int defaultExpand) throws IllegalAccessException {
		super(mTree, context, datas, defaultExpand);
	}

	@Override
	public View getConvertView(Node node, int position, View convertView,
			ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.treeview_item, parent,
					false);
			holder = new ViewHolder();
			holder.ivItemIcon = (ImageView) convertView
					.findViewById(R.id.item_icon);
			holder.tvItemLabel = (TextView) convertView
					.findViewById(R.id.item_label);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		if (node.getIcon() == -1) {
			holder.ivItemIcon.setVisibility(View.INVISIBLE);
		} else {
			holder.ivItemIcon.setVisibility(View.VISIBLE);
			holder.ivItemIcon.setImageResource(node.getIcon());
		}
		holder.tvItemLabel.setText(node.getName());
		return convertView;
	}

	private class ViewHolder {

		public TextView tvItemLabel;
		public ImageView ivItemIcon;

	}
}
