package com.example.assertmanager.adapter;

import java.util.List;

import com.example.myqr_codescan.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GroupItemAdapter extends ArrayAdapter<String> {

	private LayoutInflater mInflater;

	public GroupItemAdapter(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.group_item_layout, null);

		}

		TextView contentTv = (TextView) convertView
				.findViewById(R.id.group_item_tv);
		contentTv.setText(getItem(position));

		return convertView;
	}

}
