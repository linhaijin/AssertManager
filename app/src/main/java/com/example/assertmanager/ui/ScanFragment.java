package com.example.assertmanager.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.assertmanager.util.Constant;
import com.example.myqr_codescan.R;

public class ScanFragment extends Fragment implements OnClickListener {

	private Button shepinBtn;

	private Button jiguangBtn;

	private Button scanBtn;

	private Activity parentActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = activity;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_scan, null);

		shepinBtn = (Button) view.findViewById(R.id.shepin_fragment_btn);
		jiguangBtn = (Button) view.findViewById(R.id.jiguang_fragment_btn);
		scanBtn = (Button) view.findViewById(R.id.scan_fragment_btn);

		shepinBtn.setOnClickListener(this);
		jiguangBtn.setOnClickListener(this);
		scanBtn.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.shepin_fragment_btn:

			Intent toShepin = new Intent(parentActivity, ShepinActivity.class);
			// 0表示检索，1表示清查
			toShepin.putExtra("type", Constant.CHECK);
			parentActivity.startActivity(toShepin);

			break;
		case R.id.jiguang_fragment_btn:

			Intent toLaser = new Intent(parentActivity,
					LaserCheckActivity.class);
			toLaser.putExtra("type", Constant.CHECK);
			parentActivity.startActivity(toLaser);

			break;
		case R.id.scan_fragment_btn:

			Intent toSweepActivity = new Intent(parentActivity,
					MipcaActivityCapture.class);
			toSweepActivity.putExtra("type", Constant.CHECK);
			parentActivity.startActivity(toSweepActivity);

			break;

		default:
			break;
		}

	}

}
