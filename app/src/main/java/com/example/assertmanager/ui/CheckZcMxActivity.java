package com.example.assertmanager.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assertmanager.db.DBManager;
import com.example.assertmanager.model.ChangeZcBean;
import com.example.assertmanager.model.ZcBean;
import com.example.assertmanager.util.BaseActivity;
import com.example.assertmanager.util.CheckQcrqUtil;
import com.example.assertmanager.util.DBLogUtil;
import com.example.myqr_codescan.R;

/**
 * 手工检索进入的资产详细界面
 * 
 * @author Xingfeng
 *
 */
public class CheckZcMxActivity extends BaseActivity {

	private ZcBean bean;

	private Button confirmBtn;

	private ProgressDialog progressDialog;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			// 查询成功
			case 0:

				if (!bean.getMxsybm().contains(CheckMainActivity.sybmStr)) {

					Toast.makeText(CheckZcMxActivity.this,
							"当前资产不属于" + CheckMainActivity.sybmStr,
							Toast.LENGTH_SHORT).show();

					CheckZcMxActivity.this.finish();

					return;
				}

				String bh = bean.getMxbh();
				bh = bh.substring(bh.length() - 18, bh.length());
				((TextView) findViewById(R.id.zc_bh_show)).setText(bh);
				((TextView) findViewById(R.id.zc_km_show))
						.setText(bean.getKm());
				((TextView) findViewById(R.id.zc_mc_show))
						.setText(bean.getMc());
				((TextView) findViewById(R.id.zc_ggxh_show)).setText(bean
						.getGgxh());
				((TextView) findViewById(R.id.zc_glbm_show)).setText(bean
						.getMxxglbm());
				((TextView) findViewById(R.id.zc_sybm_show)).setText(bean
						.getMxsybm());

				confirmBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						// 更新数据库，并将记录写进日志文件

						progressDialog.setMessage("保存数据中...");
						progressDialog.show();

						new Thread(new Runnable() {

							@Override
							public void run() {

								CheckQcrqUtil.checkQcrq();

								bean.setQcsl(1);
								bean.setQcrq(CheckMainActivity.qcrq);
								DBManager.getInstance().update(bean);
								ChangeZcBean changeZcBean = new ChangeZcBean();
								changeZcBean.setMxbh(bean.getMxbh());
								changeZcBean.setQcbfbz(bean.getQcbfbz());
								changeZcBean.setQcsl(1);
								changeZcBean.setQctmbz(bean.getQctmbz());
								changeZcBean.setQctpbz(bean.getQctpbz());
								changeZcBean.setQctzbz(bean.getQctzbz());
								DBLogUtil.getInstance().write(changeZcBean);

								mHandler.sendEmptyMessage(2);
							}
						}).start();

					}
				});

				break;
			// 查询失败
			case 1:

				Toast.makeText(CheckZcMxActivity.this, "未查找到该编号资产!",
						Toast.LENGTH_SHORT).show();

				CheckZcMxActivity.this.finish();

				break;

			// 更新数据库以及记录日志文件成功
			case 2:

				Toast.makeText(CheckZcMxActivity.this, "清查成功!",
						Toast.LENGTH_SHORT).show();

				finish();

				break;
			default:
				break;
			}

			closeDialog();

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkzcmx);

		confirmBtn = (Button) findViewById(R.id.check_zc_confim_btn);

		showDialog();

		queryData();

	}

	private void queryData() {

		new Thread(new Runnable() {

			@Override
			public void run() {

				String bh = getIntent().getStringExtra("zcbh");
				bean = DBManager.getInstance().query(bh);

				if (bean != null)
					mHandler.sendEmptyMessage(0);
				else
					mHandler.sendEmptyMessage(1);

			}
		}).start();

	}

	private void showDialog() {
		if (progressDialog == null)
			progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("查询数据中...");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	private void closeDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

}
