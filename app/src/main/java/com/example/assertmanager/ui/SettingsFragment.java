package com.example.assertmanager.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.assertmanager.util.CompressUtil;
import com.example.myqr_codescan.R;

/**
 * 清查Fragment
 * 
 * @author Xingfeng
 *
 */
public class SettingsFragment extends Fragment {

	/**
	 * 生成输出文件
	 */
	private RelativeLayout geneOutputFileRl;

	private ProgressDialog progessDialog;

	private Activity parentActivity;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:

				Toast.makeText(parentActivity, "生成文件成功!", Toast.LENGTH_SHORT)
						.show();

				break;

			case 1:
				Toast.makeText(parentActivity, "生成文件失败!", Toast.LENGTH_SHORT)
						.show();

				break;

			default:
				break;
			}

			if (progessDialog != null)
				progessDialog.dismiss();
		};
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_settings, container,
				false);

		geneOutputFileRl = (RelativeLayout) view
				.findViewById(R.id.gene_data_rl);
		geneOutputFileRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				progessDialog = new ProgressDialog(parentActivity);
				progessDialog.setMessage("正在生成输出文件...");
				progessDialog.show();

				new Thread(new Runnable() {

					@Override
					public void run() {

						if (CompressUtil.compress()) {

							mHandler.sendEmptyMessage(0);

						} else {

							mHandler.sendEmptyMessage(1);

						}

					}
				}).start();

			}
		});

		return view;
	}
}
