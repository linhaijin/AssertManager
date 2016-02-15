package com.example.assertmanager.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android_serialport_api.SerialPortManager;
import android_serialport_api.UHFHXAPI;

import com.example.assertmanager.db.DBManager;
import com.example.assertmanager.model.ChangeZcBean;
import com.example.assertmanager.model.ZcBean;
import com.example.assertmanager.util.BaseActivity;
import com.example.assertmanager.util.CheckQcrqUtil;
import com.example.assertmanager.util.Constant;
import com.example.assertmanager.util.DBLogUtil;
import com.example.assertmanager.util.DataUtils;
import com.example.assertmanager.view.TopBar;
import com.example.assertmanager.view.TopBar.OnLeftClickListener;
import com.example.myqr_codescan.R;

public class ShepinActivity extends BaseActivity {

//	/**
//	 * 是否有射频模块
//	 */
//	private static boolean hasShepinModule;
//
//	static {
//		try {
//			System.loadLibrary("libserial_port");
//		} catch (Error e) {
//
//			Toast.makeText(MyApplication.wholeContext, "该设备不具备射频模块",
//					Toast.LENGTH_SHORT).show();
//			hasShepinModule = false;
//
//		}
//
//	}

	public final static int MSG_SHOW_EPC_INFO = 1;
	public final static int MSG_DISMISS_CONNECT_WAIT_SHOW = 2;
	public final static int CHECK_SUCCESS = 3;

	private UHFHXAPI api;

	//清查任务池
	private ExecutorService checkTaskPools=null;
	
	@SuppressLint("HandlerLeak")
	class StartHander extends Handler {
		WeakReference<Activity> mActivityRef;

		StartHander(Activity activity) {
			mActivityRef = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			Activity activity = mActivityRef.get();
			if (activity == null) {
				return;
			}

			switch (msg.what) {
			// 成功读取
			case MSG_SHOW_EPC_INFO:
				handleResult((String) msg.obj);
				break;

			// 连接
			case MSG_DISMISS_CONNECT_WAIT_SHOW:

				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				// 成功打开模块
				if ((Boolean) msg.obj) {
					Toast.makeText(activity, "成功开启射频模块", Toast.LENGTH_SHORT)
							.show();
					zcBhList=new ArrayList<String>();
					// 开始扫描资产
					mPool.execute(task);

				} else {
					Toast.makeText(activity, "开启射频模块失败", Toast.LENGTH_SHORT)
							.show();

					AlertDialog.Builder builder = new Builder(
							ShepinActivity.this);
					builder.setTitle("提示");
					builder.setMessage("是否尝试再次开启射频模块?");
					builder.setNegativeButton("否", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
							finish();

						}
					});
					builder.setPositiveButton("是", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							openModule();

						}
					});

					builder.create().show();
				}
				break;
			// 清查成功
			case CHECK_SUCCESS:

				numTv.setText(String.format("已清查%d件", num));

				break;

			}
		}
	};

	private Handler hMsg = new StartHander(this);

	/**
	 * 扫描成功时播放音效
	 */
	private MediaPlayer mMediaPlayer = null;

	/**
	 * 线程池
	 */
	private ExecutorService mPool;

	/**
	 * 类型：0表示检索，1表示清查
	 */
	private int type;

	/**
	 * 保存扫描到的标签
	 */
	private List<String> zcBhList;

	/**
	 * 扫描提示框
	 */
	private TextView shepinSacnPromtTv;

	/**
	 * 显示扫描数量
	 */
	private TextView numTv;

	/**
	 * 控制按钮
	 */
	private Button controlBtn;

	/**
	 * 扫描成功数量
	 */
	private int num = 0;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		if (!hasShepinModule) {
//
//			finish();
//
//		}

		setContentView(R.layout.activity_shepin);
		initDatas();
		initViews();
		api = new UHFHXAPI();
	}

	@Override
	protected void onResume() {
		isStop = false;
		mPool = Executors.newSingleThreadExecutor();
		mMediaPlayer = MediaPlayer.create(this, R.raw.ok);
		super.onResume();
		SerialPortManager.getInstance().openSerialPort();
		openModule();
	}

	@Override
	protected void onPause() {
		mMediaPlayer.release();
		mMediaPlayer = null;
		isStop = true;
		mPool.shutdown();
		mPool = null;
		super.onPause();
		SerialPortManager.getInstance().closeSerialPort();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 打开视频模块
	 */
	private void openModule() {

		showProgress();
		new Thread(new Runnable() {

			@Override
			public void run() {

				Message closemsg = new Message();
				closemsg.obj = api.open();
				closemsg.what = MSG_DISMISS_CONNECT_WAIT_SHOW;
				hMsg.sendMessage(closemsg);

			}
		}).start();

	}

	/**
	 * 播放音效，并处理扫描结果
	 * 
	 * @param barcode
	 *            扫描结果
	 */
	private void handleResult(String barcode) {

		if (mMediaPlayer == null) {
			return;
		}
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.seekTo(0);
		} else {
			mMediaPlayer.start();
		}

		// 检索,不需要查询数据库进行更新操作
		if (type == Constant.RETRIEVE) {

			if (!zcBhList.contains(barcode)) {
				zcBhList.add(barcode);
				numTv.setText(String.format("已检索%d件", ++num));
			}
		
		}
		// 清查,需要查询数据库更新清查标志位
		else if (type == Constant.CHECK) {
			checkTaskPools.execute(new HandlerTask(barcode));
		}
		
		try {
			TimeUnit.SECONDS.sleep(2);
			// 开始下一轮检索或清查
			mPool.execute(task);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

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

						hMsg.sendEmptyMessage(CHECK_SUCCESS);

					}
				}

			}

		}
	}

	private boolean isStop;
	private Runnable task = new Runnable() {

		@Override
		public void run() {
			api.startAutoRead(0x22, new byte[] { 0x00, 0x01 },
					new UHFHXAPI.AutoRead() {

						@Override
						public void timeout() {

							Toast.makeText(ShepinActivity.this, "读取超时",
									Toast.LENGTH_SHORT).show();

						}

						@Override
						public void start() {
							Log.i("whw", "start");
						}

						@Override
						public void processing(byte[] data) {
							String epc = DataUtils.toHexString(data).substring(
									4);
							hMsg.obtainMessage(MSG_SHOW_EPC_INFO, epc)
									.sendToTarget();
							Log.i("whw", "data=" + epc);
						}

						@Override
						public void end() {

							Log.i("whw", "end");
							Log.i("whw", "isStop=" + isStop);
							if (!isStop) {
								mPool.execute(task);
							}
						}
					});

		}
	};

	private void initDatas() {

		type = getIntent().getIntExtra("type", 0);
		if(type==Constant.CHECK){
			checkTaskPools=Executors.newSingleThreadExecutor();
		}
	}

	private void initViews() {

		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		topBar.setTitle("射频扫描");
		topBar.showLeftIv();
		topBar.setLeftListener(new OnLeftClickListener() {

			@Override
			public void onLeftClick(View v) {

				onBackPressed();

			}
		});

		shepinSacnPromtTv = (TextView) findViewById(R.id.shepin_tv_promt);
		numTv = (TextView) findViewById(R.id.shepin_num_tv);
		controlBtn = (Button) findViewById(R.id.control_shepin_check);
		controlBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (type == Constant.CHECK) {

					Intent showCheckResult = new Intent(ShepinActivity.this,
							CheckResultShowActivity.class);
					showCheckResult.putStringArrayListExtra(
							Constant.CHECK_RESULT_LIST,
							(ArrayList<String>) zcBhList);
					startActivity(showCheckResult);

				} else if (type == Constant.RETRIEVE) {

					Intent showRetrieveResult = new Intent(ShepinActivity.this,
							RetrieveResultShowActivity.class);
					showRetrieveResult.putStringArrayListExtra(
							Constant.RETRIEVE_RESULT_LIST,
							(ArrayList<String>) zcBhList);
					startActivity(showRetrieveResult);

				}

			}
		});

		if (type == Constant.CHECK) {
			shepinSacnPromtTv.setText("正在射频清查中");
			numTv.setText("已清查" + num + "件");
		} else if (type == Constant.RETRIEVE) {
			shepinSacnPromtTv.setText("正在射频检索中");
			numTv.setText("已检索" + num + "件");
		}
	}

	
	@Override
	public void onBackPressed() {
			
			Intent toCheckMain = new Intent(ShepinActivity.this,
					CheckMainActivity.class);
			toCheckMain.putExtra(Constant.ZC_SYBM, "");
			toCheckMain.putExtra(Constant.ZC_KM, "");
			startActivity(toCheckMain);		
		    finish();
	}
	
	private void showProgress() {

		if (progressDialog == null) {

			progressDialog = new ProgressDialog(this);
			progressDialog.setTitle("提示");
			progressDialog.setMessage("正在打开射频模块...");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

	}
}
