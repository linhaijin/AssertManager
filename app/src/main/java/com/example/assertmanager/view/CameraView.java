package com.example.assertmanager.view;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * A camera background
 *
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder;

	@SuppressWarnings("deprecation")
	private Camera mCamera;

	private Context mContext;

	public CameraView(Context context, Camera camera) {
		super(context);
		this.mContext = context;
		mCamera = camera;

		mCamera.setDisplayOrientation(90);

		// setCameraDisplayOrientation((Activity) context, 0, mCamera);

		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		try {
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPictureFormat(ImageFormat.JPEG);
			parameters.set("jpeg-quality", 100);
			//parameters.set("rotation", 90);
			WindowManager wm = ((Activity) mContext).getWindowManager();
			Display display = wm.getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);

			int screenWidth = metrics.widthPixels;
			int screenHeight = metrics.heightPixels;

			parameters.setPictureSize(screenWidth, screenHeight);

			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		if (holder.getSurface() == null)
			return;

		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

}
