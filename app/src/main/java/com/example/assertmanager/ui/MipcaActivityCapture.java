package com.example.assertmanager.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assertmanager.db.DBManager;
import com.example.assertmanager.model.ChangeZcBean;
import com.example.assertmanager.model.ZcBean;
import com.example.assertmanager.util.ActivityCollector;
import com.example.assertmanager.util.CheckQcrqUtil;
import com.example.assertmanager.util.Constant;
import com.example.assertmanager.util.DBLogUtil;
import com.example.assertmanager.view.TopBar;
import com.example.assertmanager.view.TopBar.OnLeftClickListener;
import com.example.myqr_codescan.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;

public class MipcaActivityCapture extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	/**
	 * 0表示检索，1表示清查
	 */
	private int type;
	/**
	 * 扫描数目
	 */
	private int scanNum = 0;

	private TextView scanNumTv;

	private Button controlScanCheckBtn;
	/**
	 * 缓存清查的编号
	 */
	private List<String> bhList = new ArrayList<String>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		CameraManager.init(getApplication());

		type = getIntent().getIntExtra("type", Constant.CHECK);

		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		topBar.setTitle("条码扫描");
		topBar.showLeftIv();
		topBar.setLeftListener(new OnLeftClickListener() {

			@Override
			public void onLeftClick(View v) {

				onBackPressed();

			}
		});

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		initOtherViews();

	}

	/**
	 * 设置提示框和按钮的位置
	 */
	private void initOtherViews() {

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int screenWidth = metrics.widthPixels;
		int screenHeight = metrics.heightPixels;
		float density = metrics.density;

		RelativeLayout parent = (RelativeLayout) findViewById(R.id.mipca_rl);

		scanNumTv = new TextView(this);
		setCheckText(scanNum);
		scanNumTv.setTextColor(Color.WHITE);
		scanNumTv.setGravity(Gravity.CENTER);
		scanNumTv.setBackgroundResource(R.drawable.orange_border_pressed_bg);
		scanNumTv.setY(screenHeight / 2 + screenWidth / 3 + 20 * density);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		parent.addView(scanNumTv, lp);

		controlScanCheckBtn = new Button(this);
		controlScanCheckBtn.setText("停止扫描");
		controlScanCheckBtn.setTextColor(Color.WHITE);
		controlScanCheckBtn.setTextSize(18);
		controlScanCheckBtn.setWidth((int) (240 * density));
		controlScanCheckBtn.setGravity(Gravity.CENTER);
		controlScanCheckBtn
				.setBackgroundResource(R.drawable.check_main_button_unpressed);
		controlScanCheckBtn.setY(scanNumTv.getY() + scanNumTv.getHeight() + 50
				* density);
		lp = new LayoutParams((int) (240 * density), LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		parent.addView(controlScanCheckBtn, lp);
		controlScanCheckBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (type == Constant.CHECK) {

					Intent showCheckResult = new Intent(
							MipcaActivityCapture.this,
							CheckResultShowActivity.class);
					showCheckResult.putStringArrayListExtra(
							Constant.CHECK_RESULT_LIST,
							(ArrayList<String>) bhList);
					startActivity(showCheckResult);

				} else if (type == Constant.RETRIEVE) {

					Intent showRetrieveResult = new Intent(
							MipcaActivityCapture.this,
							RetrieveResultShowActivity.class);
					showRetrieveResult.putStringArrayListExtra(
							Constant.RETRIEVE_RESULT_LIST,
							(ArrayList<String>) bhList);
					startActivity(showRetrieveResult);

				}

			}
		});

	}

	private void setCheckText(int checkNum2) {

		String text = "";

		if (type == Constant.CHECK) {

			text = String.format("已清查%d件", checkNum2);

		} else if (type == Constant.RETRIEVE) {
			text = String.format("已检索%d件", checkNum2);
		}

		scanNumTv.setText(text);

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private boolean first = true;

	/**
	 * 处理扫描的结果
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {

		if (first) {
			inactivityTimer.onActivity();
			first = false;

		}

		playBeepSoundAndVibrate();
		String resultString = result.getText();
		onResultHandler(resultString, barcode);
	}

	/**
	 * @param resultString
	 * @param bitmap
	 */
	private void onResultHandler(String resultString, Bitmap bitmap) {
		if (TextUtils.isEmpty(resultString)) {
			Toast.makeText(MipcaActivityCapture.this, "Scan failed!",
					Toast.LENGTH_SHORT).show();
			return;
		}

		Toast.makeText(MipcaActivityCapture.this, "扫描结果：" + resultString,
				Toast.LENGTH_SHORT).show();

		queryDataAndUpdate(resultString);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			// 更新成功
			case 0:

				++scanNum;
				setCheckText(scanNum);

				break;
			// 更新失败
			case 1:

				Toast.makeText(MipcaActivityCapture.this, "清查失败!",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

		};
	};

	/**
	 * 查询数据并且更新
	 * 
	 * @param bh
	 */
	private void queryDataAndUpdate(final String bh) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				synchronized (bhList) {

					// 已在本次清查中清查
					if (bhList.contains(bh))
						return;

					ZcBean bean = DBManager.getInstance().query(bh);

					if (bean != null && bean.getQcsl() == 0) {

						bhList.add(bh);
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
					} else {
						mHandler.sendEmptyMessage(1);
					}

				}

			}
		}).start();

	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	@Override
	public void onBackPressed() {

		// if (ActivityCollector.getCurrentActivity() instanceof MainActivity) {
		//
		// Intent toCheckMain = new Intent(MipcaActivityCapture.this,
		// CheckMainActivity.class);
		// toCheckMain.putExtra(Constant.ZC_SYBM, "");
		// toCheckMain.putExtra(Constant.ZC_KM, "");
		// startActivity(toCheckMain);
		//
		// }

		if (type == Constant.RETRIEVE) {

			Intent toRetrieveMain = new Intent(MipcaActivityCapture.this,
					RetrieveMainActivity.class);
			toRetrieveMain.putExtra(Constant.ZC_SYBM, "");
			toRetrieveMain.putExtra(Constant.ZC_KM, "");
			startActivity(toRetrieveMain);
			finish();

		} else if (type == Constant.CHECK) {
			Intent toCheckMain = new Intent(MipcaActivityCapture.this,
					CheckMainActivity.class);
			toCheckMain.putExtra(Constant.ZC_SYBM, "");
			toCheckMain.putExtra(Constant.ZC_KM, "");
			startActivity(toCheckMain);
			finish();
		}
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.ok);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}