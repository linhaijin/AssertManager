package com.example.assertmanager.ui;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.example.assertmanager.model.ZcBean;
import com.example.assertmanager.util.BaseActivity;
import com.example.assertmanager.util.Constant;
import com.example.assertmanager.view.TopBar;
import com.example.assertmanager.view.TopBar.OnLeftClickListener;
import com.example.myqr_codescan.R;

public class ImageShowActivity extends BaseActivity {

	private ImageView mImageShowIv;

	private Button mCaptureAgainBtn;

	private ZcBean bean;

	/**
	 * 重拍过的标志,0表示没有重拍，1表示重拍过
	 */
	private int captureAgainFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_show);

		initViews();

		initDatas();

		mCaptureAgainBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Intent captureIntent = new Intent(
				// MediaStore.ACTION_IMAGE_CAPTURE);
				// startActivityForResult(captureIntent, 0);
				Intent captureIntent = new Intent(ImageShowActivity.this,
						CapturePictureActivity.class);
				captureIntent.putExtra(Constant.ZC_BEAN_KEY, bean);
				startActivityForResult(captureIntent, 0);

			}
		});
	}

	/**
	 * 处理拍照成功返回的照片
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO 将图片保存到“改变图片”目录中，并更新“图片”目录中的图片，记录日志文件
		if (requestCode == 0 && resultCode == RESULT_OK) {

			captureAgainFlag = 1;

			String imagePath = Constant.PICTURE_PATH + bean.getId() + ".jpg";
			setImage(imagePath);

		}
	}

	/**
	 * 调整图片旋转角度，设置图片
	 * 
	 * @param imagePath
	 *            图片路径
	 */
	private void setImage(String imagePath) {

		// 调整角度
		int degree = 0;
		try {
			ExifInterface exif = new ExifInterface(imagePath);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;

			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			default:
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		Bitmap returnBitmap = null;
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		returnBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		if (returnBitmap == null)
			returnBitmap = bitmap;
		if (bitmap != returnBitmap)
			bitmap.recycle();

		LayoutParams lp = (LayoutParams) mImageShowIv.getLayoutParams();
		int width = lp.width;
		int height = bitmap.getHeight() * width / bitmap.getHeight();
		lp.height = height;
		mImageShowIv.setLayoutParams(lp);
		mImageShowIv.setImageBitmap(returnBitmap);
	}

	private void initDatas() {

		bean = (ZcBean) getIntent().getSerializableExtra(Constant.ZC_BEAN_KEY);

		String imagePath = Constant.PICTURE_PATH + bean.getId() + ".jpg";
		File imageFile = new File(Constant.PICTURE_PATH + bean.getId() + ".jpg");
		if (imageFile.exists()) {

			setImage(imagePath);

		} else {
			Intent captureIntent = new Intent(ImageShowActivity.this,
					CapturePictureActivity.class);
			captureIntent.putExtra(Constant.ZC_BEAN_KEY, bean);
			startActivityForResult(captureIntent, 0);
		}

	}

	private void initViews() {

		mImageShowIv = (ImageView) findViewById(R.id.imageShowIv);
		mCaptureAgainBtn = (Button) findViewById(R.id.capture_again_btn);

		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		topBar.setTitle("资产图片");
		topBar.showLeftIv();
		topBar.setLeftListener(new OnLeftClickListener() {

			@Override
			public void onLeftClick(View v) {

				Intent intent = new Intent(ImageShowActivity.this,
						ZcMxActivity.class);
				intent.putExtra(Constant.PICTURE_CHANGE_FLAG, captureAgainFlag);
				startActivityForResult(intent, 0);
				finish();

			}
		});
	}
}
