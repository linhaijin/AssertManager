package com.example.assertmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.assertmanager.util.BaseActivity;
import com.example.assertmanager.util.FindDbFileUtil;
import com.example.myqr_codescan.R;

public class SplashActivity extends BaseActivity {

	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);		
		
		// 查找DB文件
		new Thread(new Runnable() {

			@Override
			public void run() {

				FindDbFileUtil.getDbPath();

			}
		}).start();

		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {

				startActivity(new Intent(SplashActivity.this,
						MainActivity.class));
				finish();

			}
		}, 2000);

	}

}
