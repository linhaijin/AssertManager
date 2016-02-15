package com.example.assertmanager.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.assertmanager.db.DBManager;
import com.example.assertmanager.model.ChangeZcBean;
import com.example.assertmanager.model.ZcBean;
import com.example.assertmanager.util.BaseActivity;
import com.example.assertmanager.util.CheckQcrqUtil;
import com.example.assertmanager.util.Constant;
import com.example.assertmanager.util.DBLogUtil;
import com.example.assertmanager.view.TopBar;
import com.example.assertmanager.view.TopBar.OnLeftClickListener;
import com.example.myqr_codescan.R;

public class LaserCheckActivity extends BaseActivity {

	public static final String SCANNER_POWER_ON = "SCANNER_POWER_ON";
	public static final String SCANNER_OUTPUT_MODE = "SCANNER_OUTPUT_MODE";
	public static final String SCANNER_TERMINAL_CHAR = "SCANNER_TERMINAL_CHAR";
	public static final String SCANNER_PREFIX = "SCANNER_PREFIX";
	public static final String SCANNER_SUFFIX = "SCANNER_SUFFIX";
	public static final String SCANNER_VOLUME = "SCANNER_VOLUME";
	public static final String SCANNER_TONE = "SCANNER_TONE";
	public static final String SCANNER_PLAYTONE_MODE = "SCANNER_PLAYTONE_MODE";
	private static WakeLock wakeLock = null;

	/**
	 * 提示栏
	 */
	private TextView laserPromtIv;

	/**
	 * 扫描数目
	 */
	private TextView checkNumTv;

	/**
	 * 控制激光扫描的按钮
	 */
	private Button controlLaserCheckBtn;

	/**
	 * 扫描类型，0表示检索，1表示清查
	 */
	private int type;

	private boolean isStop = false;

	/**
	 * 保存扫描到的资产编号，避免重复添加与修改
	 */
	private List<String> zcBhList;

	private MediaPlayer mediaPlayer = null;

	// 清查任务池
	private ExecutorService checkTaskPools = null;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Log.i("whw", "getAction=" + intent.getAction().toString());
			if (intent.getAction().equals(
					"com.android.server.scannerservice.broadcast")) {
				String barcode = "";
				barcode = intent.getExtras().getString("scannerdata");
				handleResult(barcode);
			}
		}
	};

	private int checkNum = 0;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			// 扫描成功
			if (msg.what == 0) {

				++checkNum;
				checkNumTv.setText("已清查" + checkNum + "件");

			}

		};
	};

	/**
	 * 处理扫描结果的线程
	 * 
	 * @author Xingfeng
	 *
	 */
	class HandlerTask implements Runnable {

		private String result;

		public HandlerTask(String result) {
			super();
			this.result = result;
		}

		@Override
		public void run() {

			synchronized (zcBhList) {

				ZcBean bean = null;

				// 不含result。则进行判断
				if (result != null && !zcBhList.contains(result)) {

					bean = DBManager.getInstance().query(result);

					if (bean != null && bean.getQcsl() == 0) {

						zcBhList.add(result);

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

						mHandler.sendEmptyMessage(0);

					}
				}

				try {
					TimeUnit.SECONDS.sleep(2);
					// 开始下一轮清查
					startScanZc();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_laser_check);
		initDatas();
		initViews();

	}

	/**
	 * 播放音效，并处理扫描结果
	 * 
	 * @param barcode
	 *            扫描结果
	 */
	private void handleResult(String barcode) {

		if (mediaPlayer == null) {
			return;
		}
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(0);
		} else {
			mediaPlayer.start();
		}

		// 检索,不需要查询数据库进行更新操作
		if (type == Constant.RETRIEVE) {

			if (!zcBhList.contains(barcode)) {
				zcBhList.add(barcode);

				checkNumTv.setText(String.format("已检索%d件", ++checkNum));
			}

		}
		// 清查,需要查询数据库更新清查标志位
		else if (type == Constant.CHECK) {

			checkTaskPools.execute(new HandlerTask(barcode));

		}

		try {
			TimeUnit.SECONDS.sleep(2);
			// 开始下一轮检索
			startScanZc();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 开始扫描资产
	 */
	private void startScanZc() {

		if (!isStop) {

			Intent intent = new Intent(
					"android.intent.action.SCANNER_BUTTON_DOWN", null);
			sendOrderedBroadcast(intent, null);

		}

	}

	private void pauseCheckZc() {

		if (isStop) {

			Intent endIntent = new Intent(
					"android.intent.action.SCANNER_BUTTON_UP", null);
			sendOrderedBroadcast(endIntent, null);

		}

	}

	private void initDatas() {

		IntentFilter filter = new IntentFilter(
				"com.android.server.scannerservice.broadcast");
		registerReceiver(receiver, filter);
		zcBhList = new ArrayList<String>();

	}

	private void initViews() {

		laserPromtIv = (TextView) findViewById(R.id.laser_tv_promt);
		checkNumTv = (TextView) findViewById(R.id.laser_check_num_tv);
		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		topBar.setTitle("激光扫描");
		topBar.showLeftIv();
		topBar.setLeftListener(new OnLeftClickListener() {

			@Override
			public void onLeftClick(View v) {

				onBackPressed();

			}
		});

		controlLaserCheckBtn = (Button) findViewById(R.id.control_laser_check);
		controlLaserCheckBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (type == Constant.CHECK) {

					Intent showCheckResult = new Intent(
							LaserCheckActivity.this,
							CheckResultShowActivity.class);
					showCheckResult.putStringArrayListExtra(
							Constant.CHECK_RESULT_LIST,
							(ArrayList<String>) zcBhList);
					startActivity(showCheckResult);

				} else if (type == Constant.RETRIEVE) {

					Intent showRetrieveResult = new Intent(
							LaserCheckActivity.this,
							RetrieveResultShowActivity.class);
					showRetrieveResult.putStringArrayListExtra(
							Constant.RETRIEVE_RESULT_LIST,
							(ArrayList<String>) zcBhList);
					startActivity(showRetrieveResult);

				}

			}

		});

		// 根据扫描类型更改相关文字
		type = getIntent().getIntExtra("type", 0);
		if (type == Constant.RETRIEVE) {
			laserPromtIv.setText("正在激光检索中");
			checkNumTv.setText("已检索" + checkNum + "件");
		} else if (type == Constant.CHECK) {

			checkTaskPools = Executors.newSingleThreadExecutor();

			laserPromtIv.setText("正在激光清查中");
			checkNumTv.setText("已清查" + checkNum + "件");
		}
	}

	@Override
	protected void onResume() {

		mediaPlayer = MediaPlayer.create(this, R.raw.ok);

		super.onResume();
		acquireWakeLock();

		// 开始清查
		isStop = false;
		startScanZc();

	}

	@Override
	protected void onPause() {

		mediaPlayer.release();
		mediaPlayer = null;
		super.onPause();
		isStop = true;
		pauseCheckZc();
	}

	@Override
	public void onBackPressed() {

		if (type == Constant.RETRIEVE) {

			Intent toRetrieveMain = new Intent(LaserCheckActivity.this,
					RetrieveMainActivity.class);
			toRetrieveMain.putExtra(Constant.ZC_SYBM, "");
			toRetrieveMain.putExtra(Constant.ZC_KM, "");
			startActivity(toRetrieveMain);
			finish();

		} else if (type == Constant.CHECK) {
			Intent toCheckMain = new Intent(LaserCheckActivity.this,
					CheckMainActivity.class);
			toCheckMain.putExtra(Constant.ZC_SYBM, "");
			toCheckMain.putExtra(Constant.ZC_KM, "");
			startActivity(toCheckMain);
			finish();
		}
	}

	@Override
	protected void onDestroy() {

		releaseWakeLock();
		isStop = false;
		// 停止扫描
		Intent intent = new Intent("android.intent.action.SCANNER_BUTTON_UP",
				null);
		sendOrderedBroadcast(intent, null);
		// 关闭设备
		// intent = new Intent("com.android.server.scannerservice.onoff");
		// intent.putExtra("scanneronoff", DISENABLE);
		// sendBroadcast(intent);

		if (null != receiver) {
			try {
				unregisterReceiver(receiver);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		super.onDestroy();
	}

	private void acquireWakeLock() {
		if (null == wakeLock) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
					| PowerManager.ON_AFTER_RELEASE, getClass()
					.getCanonicalName());
			if (null != wakeLock) {
				wakeLock.acquire();
			}
		}
	}

	public static void releaseWakeLock() {
		if (null != wakeLock && wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock = null;
		}
	}

}
