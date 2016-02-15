package com.example.assertmanager.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 图片工具类，用于Bitmap，byte，array，Drawable之间进行转换以及图片缩放 Created by Xingfeng on
 * 2015/8/24.
 */
public class ImageUtils {

	/**
	 * 将BItmap转换成字节
	 * 
	 * @param bitmap
	 */
	public static byte[] bitmapToByte(Bitmap bitmap) {

		if (bitmap == null)
			return null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		bitmap.recycle();

		if (baos.size() != 0)
			return baos.toByteArray();

		return null;

	}

	/**
	 * 将字节转换成Bitmap类型的图片
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] b) {

		if (b == null)
			return null;

		return BitmapFactory.decodeByteArray(b, 0, b.length);

	}

	/**
	 * 将Bitmap转换成Drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Drawable bitmapToDrawable(Bitmap bitmap) {

		return bitmap == null ? null : new BitmapDrawable(bitmap);

	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		return drawable == null ? null : ((BitmapDrawable) drawable)
				.getBitmap();

	}

	/**
	 * 将字节转换成Drawable
	 * 
	 * @param b
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] b) {

		return bitmapToDrawable(byteToBitmap(b));

	}

	/**
	 * 将Drawable对象转换成byte数据
	 * 
	 * @param drawable
	 * @return
	 */
	public static byte[] drawableToByte(Drawable drawable) {

		return bitmapToByte(drawableToBitmap(drawable));

	}

	/**
	 * 从网络的图片地址得到输入流，你需要自己去关闭这个输入流
	 * 
	 * @param imageUrl
	 *            图片的网络地址
	 * @param readTimeOutMillis
	 *            读取超时时间
	 * @return 输入流
	 */
	private static InputStream getInputStreamFromUrl(String imageUrl,
			int readTimeOutMillis) {

		InputStream in = null;

		HttpURLConnection conn = null;

		try {
			URL url = new URL(imageUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(readTimeOutMillis);
			in = conn.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.disconnect();
		}

		return null;

	}

	/**
	 * 通过图片地址url获取Bitmao类型的图片
	 * 
	 * @param imageUrl
	 * @param readTimeOutMills
	 * @return
	 */
	public static Bitmap getBitmaoFromUrl(String imageUrl, int readTimeOutMills) {

		Bitmap bitmap = null;
		InputStream in = getInputStreamFromUrl(imageUrl, readTimeOutMills);
		bitmap = BitmapFactory.decodeStream(in);
		closeInputStream(in);
		return bitmap;

	}

	private static void closeInputStream(InputStream in) {

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Drawable getDrawableFromUrl(String imageUrl,
			int readTimeOutMills) {

		return bitmapToDrawable(getBitmaoFromUrl(imageUrl, readTimeOutMills));

	}

	/**
	 * 设置图片大小
	 * 
	 * @param org
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {

		return Bitmap.createBitmap(org, 0, 0, newWidth, newHeight);

	}

	public static Bitmap scaleImage(Bitmap org, float scaleWidth,
			float scaleHeight) {

		if (org == null)
			return null;

		int newWidth = (int) (org.getWidth() * scaleWidth);
		int newHeight = (int) (org.getHeight() * scaleHeight);
		return scaleImageTo(org, newWidth, newHeight);
	}

}
