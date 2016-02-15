package com.example.assertmanager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myqr_codescan.R;

public class TopBar extends FrameLayout {

	private TextView titleTv;

	private ImageView leftIv;

	private ImageView rightIv;

	private OnLeftClickListener leftListener;

	private OnRightClcikListener rightListener;

	public void setLeftListener(OnLeftClickListener leftListener) {
		this.leftListener = leftListener;
	}

	public void setRightListener(OnRightClcikListener rightListener) {
		this.rightListener = rightListener;
	}

	public interface OnLeftClickListener {
		public void onLeftClick(View v);
	}

	public interface OnRightClcikListener {
		public void onRightClick(View v);
	}

	public TopBar(final Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater.from(context).inflate(R.layout.topbar_layout, this);

		titleTv = (TextView) findViewById(R.id.title);
		leftIv = (ImageView) findViewById(R.id.leftIcon);
		rightIv = (ImageView) findViewById(R.id.rightIcon);

		leftIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (leftListener != null) {

					leftListener.onLeftClick(v);

				}
			}
		});

		rightIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (rightListener != null) {

					rightListener.onRightClick(v);

				}

			}
		});

	}

	/**
	 * 获取标题TextView
	 * 
	 * @return
	 */
	public TextView getTitleTv() {
		return titleTv;
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {

		titleTv.setText(title);

	}

	/**
	 * 显示左边ImageView
	 */
	public void showLeftIv() {

		leftIv.setVisibility(View.VISIBLE);

	}

	/**
	 * 显示右边ImageView
	 */
	public void showRightIv() {

		rightIv.setVisibility(View.VISIBLE);

	}

	public ImageView getRightIv(){
		return rightIv;
	}
	public void setRightImageResource(int resId) {

		rightIv.setImageResource(resId);

	}
}
