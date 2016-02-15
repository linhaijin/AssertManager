package com.example.assertmanager.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assertmanager.db.DBManager;
import com.example.assertmanager.model.BmBean;
import com.example.assertmanager.util.Constant;
import com.example.assertmanager.view.MutipleSpnnier;
import com.example.myqr_codescan.R;

/**
 * 清查Fragment
 * 
 * @author Xingfeng
 *
 */
public class CheckFragment extends Fragment implements OnClickListener {

	/**
	 * 使用部门Spinner
	 */
	private MutipleSpnnier<BmBean> sybmSp;

	/**
	 * 开始清查Button
	 */
	private Button startCheckBtn;

	private TextView historyContentTv;

	/**
	 * 父activity
	 */
	private Activity parentActivity;

	private ProgressDialog progressDialog;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_check, container, false);

		sybmSp = (MutipleSpnnier<BmBean>) view.findViewById(R.id.sybm_sp);
		sybmSp.setType(new BmBean(0, 0, null));
		startCheckBtn = (Button) view.findViewById(R.id.start_check_btn);
		startCheckBtn.setOnClickListener(this);

		return view;
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:

				progressDialog.dismiss();

				String qcrq = (String) msg.obj;

				if (TextUtils.isEmpty(qcrq) || qcrq.equals("NULL")) {

					Intent toCheckMain = new Intent(parentActivity,
							CheckMainActivity.class);
					toCheckMain.putExtra(Constant.ZC_SYBM,
							sybmSp.getTitle());
					startActivity(toCheckMain);

				} else {

					showPromtDialog(qcrq);

				}

				break;
			case 1:
				if (progressDialog != null)
					progressDialog.dismiss();

				Intent toCheckMain = new Intent(parentActivity,
						CheckMainActivity.class);
				toCheckMain.putExtra(Constant.ZC_SYBM,
						sybmSp.getTitle());
				startActivity(toCheckMain);
				break;
			default:
				break;
			}
		};

	};

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.start_check_btn)

			if (TextUtils.isEmpty(sybmSp.getTitle())) {

				Toast.makeText(parentActivity, "使用部门不能为空!", Toast.LENGTH_SHORT)
						.show();
				return;

			}

		if (progressDialog == null)
			progressDialog = new ProgressDialog(parentActivity);

		progressDialog.setMessage("查询历史清查记录中...");
		progressDialog.show();

		new Thread(new Runnable() {

			@Override
			public void run() {

				String qcrq = DBManager.getInstance().getQcrq(
						sybmSp.getTitle().toString());

				Message msg = Message.obtain();
				msg.obj = qcrq;
				msg.what = 0;
				mHandler.sendMessage(msg);

			}
		}).start();

	}

	/**
	 * 显示提示对话框
	 * 
	 * @param qcrq
	 */
	private void showPromtDialog(final String qcrq) {

		AlertDialog.Builder builder = new Builder(parentActivity);
		builder.setMessage("检测到您于" + qcrq + "有一次清查记录，是否要继续清查？");
		builder.setPositiveButton("继续清查",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						Intent toCheckMain = new Intent(parentActivity,
								CheckMainActivity.class);
						toCheckMain.putExtra(Constant.ZC_SYBM,
								sybmSp.getTitle());
						startActivity(toCheckMain);
					}
				});

		builder.setNegativeButton("重新开始",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();

						clearDatas(sybmSp.getTitle().toString(), qcrq);

					}

				});
		builder.create().show();

	}

	/**
	 * 清空清查数据
	 * 
	 * @param bmmc
	 *            部门名称
	 * @param qcrq
	 *            清查日期
	 */
	private void clearDatas(final String bmmc, final String qcrq) {

		if (progressDialog == null)
			progressDialog = new ProgressDialog(parentActivity);
		progressDialog.setMessage("正在清除数据，请稍等...");
		progressDialog.show();

		new Thread(new Runnable() {

			@Override
			public void run() {

				// 清空数据
				DBManager.getInstance().clearQcDatas(bmmc, qcrq);

				mHandler.sendEmptyMessage(1);

			}
		}).start();

	}
}
