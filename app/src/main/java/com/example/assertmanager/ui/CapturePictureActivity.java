package com.example.assertmanager.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.assertmanager.model.ZcBean;
import com.example.assertmanager.util.BaseActivity;
import com.example.assertmanager.util.Constant;
import com.example.assertmanager.view.CameraView;
import com.example.myqr_codescan.R;

@SuppressWarnings("deprecation")
public class CapturePictureActivity extends BaseActivity implements
		OnClickListener {

	private CameraView mCameraView;

	private Camera mCamera;

	private ImageView capturePictureIv;

	private ImageView captureBackIv;

	private ImageView captureOkIv;

	private Bitmap bitmap;

	private ZcBean zcBean;

	private Timer mTimer;

	private TimerTask mTimerTask;

	// 拍摄照片的回调接口
	private PictureCallback mPictureCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			mCamera.stopPreview();

			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

			Bitmap bMapRotate;
			Configuration config = getResources().getConfiguration();
			// 竖拍
			if (config.orientation == 1) {

				Matrix matrix = new Matrix();
				matrix.reset();
				matrix.postRotate(90);
				bMapRotate = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				bitmap = bMapRotate;

			}

			//
			// ByteArrayOutputStream bas = new ByteArrayOutputStream();
			// bitmap.compress(CompressFormat.JPEG, 100, bas);
			//
			// byte[] newData = bas.toByteArray();
			// bitmap = BitmapFactory.decodeByteArray(newData, 0,
			// newData.length);

			capturePictureIv.setVisibility(View.INVISIBLE);
			captureOkIv.setVisibility(View.VISIBLE);

		}
	};

	private AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {

			if (success) {

				mCamera.setOneShotPreviewCallback(null);

			}

		}
	};

	public Bitmap scaleDownBitmap(Bitmap photo, int newHeight) {

		final float densityMultiplier = getResources().getDisplayMetrics().density;

		int h = (int) (newHeight * densityMultiplier);
		int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));

		photo = Bitmap.createScaledBitmap(photo, w, h, true);

		return photo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_capture);

		zcBean = (ZcBean) getIntent()
				.getSerializableExtra(Constant.ZC_BEAN_KEY);

		capturePictureIv = (ImageView) findViewById(R.id.capture_picture_iv);
		captureBackIv = (ImageView) findViewById(R.id.capture_picture_back);
		captureOkIv = (ImageView) findViewById(R.id.capture_picture_ok);
		captureBackIv.setOnClickListener(this);
		capturePictureIv.setOnClickListener(this);
		captureOkIv.setOnClickListener(this);

		captureOkIv.setVisibility(View.GONE);

		FrameLayout cameraBg = (FrameLayout) findViewById(R.id.camera_bg);
		if (checkCameraHardware()) {

			mCamera = getCameraInstance();

		}

		mCameraView = new CameraView(this, mCamera);
		cameraBg.addView(mCameraView);

		mTimer = new Timer();
		mTimerTask = new CameraTimeTask();
		mTimer.schedule(mTimerTask, 0, 1500);
		// mCamera.autoFocus(autoFocusCallback);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mCamera != null)
			mCamera.release();

		if (mTimerTask != null) {
			mTimerTask.cancel();
		}
	}

	/**
	 * Check if the device has camera
	 * 
	 * @return
	 */
	private boolean checkCameraHardware() {

		if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
			return true;
		else
			return false;

	}

	@SuppressWarnings("deprecation")
	public static Camera getCameraInstance() {

		Camera camera = null;
		try {
			camera = Camera.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return camera;

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.capture_picture_iv:

			mCamera.takePicture(null, null, mPictureCallback);

			break;
		case R.id.capture_picture_back:

			if (captureOkIv.getVisibility() == View.VISIBLE) {

				mCamera.startPreview();
				capturePictureIv.setVisibility(View.VISIBLE);
				captureOkIv.setVisibility(View.GONE);

			} else {

				Intent intent = new Intent(this, ImageShowActivity.class);
				setResult(RESULT_CANCELED, intent);
				CapturePictureActivity.this.finish();

			}

			break;
		case R.id.capture_picture_ok:

			if (captureOkIv.getVisibility() == View.VISIBLE) {

				saveAndUpdate(bitmap);

			}

			break;
		default:
			break;
		}

	}

	private ProgressDialog dialog;

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			dialog.dismiss();

			switch (msg.what) {

			// 成功
			case 0:

				Intent okIntent = new Intent(CapturePictureActivity.this,
						ImageShowActivity.class);
				// okIntent.putExtra("data", bitmap);
				setResult(RESULT_OK, okIntent);
				CapturePictureActivity.this.finish();

				break;
			// 失败
			case 1:

				Intent errorIntent = new Intent(CapturePictureActivity.this,
						ImageShowActivity.class);
				// okIntent.putExtra("data", bitmap);
				setResult(RESULT_CANCELED, errorIntent);
				CapturePictureActivity.this.finish();

				break;
			default:
				break;
			}

		};

	};

	private void saveAndUpdate(final Bitmap bitmap) {

		if (dialog == null)
			dialog = new ProgressDialog(this);
		dialog.setMessage("正在保存图片...");
		dialog.show();

		new Thread(new Runnable() {

			@Override
			public void run() {

				File imageFile = new File(Constant.PICTURE_PATH
						+ zcBean.getId() + ".jpg");
				File changeImageFile = new File(Constant.CHANGE_PICTURE_PATH
						+ zcBean.getId() + ".jpg");

				File backupFile = null;

				if (imageFile.exists()) {
					backupFile = imageFile;
					imageFile.delete();
				}

				File backupChangeFile = null;
				if (changeImageFile.exists()) {
					backupChangeFile = changeImageFile;
					changeImageFile.delete();

				}

				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(imageFile);

					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

					fos = new FileOutputStream(changeImageFile);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

				} catch (FileNotFoundException e) {

					if (backupFile != null)
						backupFile.renameTo(imageFile);

					if (backupChangeFile != null)
						backupChangeFile.renameTo(changeImageFile);

					mHandler.sendEmptyMessage(1);

				} finally {
					try {
						if (fos != null)
							fos.close();
					} catch (IOException e) {
						e.printStackTrace();

					}
				}

				mHandler.sendEmptyMessage(0);

			}
		}).start();
	}

	class CameraTimeTask extends TimerTask {

		@Override
		public void run() {
			if (mCamera != null) {
				mCamera.autoFocus(autoFocusCallback);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}
}
