package com.example.assertmanager.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.assertmanager.util.ActivityCollector;
import com.example.assertmanager.view.TopBar;
import com.example.myqr_codescan.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private LinearLayout scanLL;

	private LinearLayout retrieveLl;

	private LinearLayout checkLl;

	private LinearLayout settingsLl;

	/**
	 * 扫描TextView
	 */
	private TextView scanTv;

	/**
	 * 扫描ImageView
	 */
	private ImageView scanIv;

	/**
	 * 检索TextView
	 */
	private TextView retrieveTv;

	/**
	 * 检索ImageView
	 */
	private ImageView retrieveIv;

	/**
	 * 清查TextView
	 */
	private TextView checkTv;

	/**
	 * 清查ImageView
	 */
	private ImageView checkIv;

	/**
	 * 设置TextView
	 */
	private TextView settingsTv;

	/**
	 * 设置ImageView
	 */
	private ImageView settingsIv;

	private ViewPager mViewPager;

	private FragmentPagerAdapter mAdapter;

	private List<Fragment> fragmentList;

	private TopBar mTopBar;

	private String textPressedColor = "#AF7C2E";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ActivityCollector.addActivity(this);
		setContentView(R.layout.activity_main);

		initViews();
		initDatas();
		initListeners();

		mViewPager.setCurrentItem(0);

	}

	private void initListeners() {

		scanLL.setOnClickListener(this);
		retrieveLl.setOnClickListener(this);
		checkLl.setOnClickListener(this);
		settingsLl.setOnClickListener(this);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				resetTextViews();
				setSelect(position);

			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

	}

	/**
	 * 设定选中的页面对应的TextView字体颜色为绿色
	 * 
	 * @param position
	 */
	protected void setSelect(int position) {

		switch (position) {

		case 0:

			scanTv.setTextColor(Color.parseColor(textPressedColor));
			scanIv.setImageResource(R.drawable.scan_icon_pressed);
			mTopBar.setTitle(getResources().getString(R.string.zc_scan));
			break;

		case 1:
			retrieveTv.setTextColor(Color.parseColor(textPressedColor));
			retrieveIv.setImageResource(R.drawable.retrieve_icon_pressed);
			mTopBar.setTitle(getResources()
					.getString(R.string.deviece_retrieve));
			break;
		case 2:
			checkTv.setTextColor(Color.parseColor(textPressedColor));
			checkIv.setImageResource(R.drawable.check_icon_pressed);
			mTopBar.setTitle(getResources().getString(R.string.zc_check));
			break;

		case 3:

			settingsTv.setTextColor(Color.parseColor(textPressedColor));
			settingsIv.setImageResource(R.drawable.settings_icon_pressed);
			mTopBar.setTitle(getResources().getString(R.string.sys_settings));

			break;
		default:
			break;
		}

	}

	/**
	 * 重置TextView，令所有颜色变为白色，并且让图标变化未按下的状态
	 */
	protected void resetTextViews() {

		scanTv.setTextColor(Color.WHITE);
		retrieveTv.setTextColor(Color.WHITE);
		checkTv.setTextColor(Color.WHITE);
		settingsTv.setTextColor(Color.WHITE);

		scanIv.setImageResource(R.drawable.scan_icon_unpressed);
		retrieveIv.setImageResource(R.drawable.retrieve_icon_unpressed);
		checkIv.setImageResource(R.drawable.check_icon_unpressed);
		settingsIv.setImageResource(R.drawable.settings_icon_unpressed);

	}

	private void initDatas() {

		fragmentList = new ArrayList<Fragment>();

		fragmentList.add(new ScanFragment());
		fragmentList.add(new RetrieveFragment());
		fragmentList.add(new CheckFragment());
		fragmentList.add(new SettingsFragment());

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return fragmentList.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return fragmentList.get(arg0);
			}
		};

		mViewPager.setAdapter(mAdapter);

	}

	private void initViews() {

		scanLL = (LinearLayout) findViewById(R.id.id_ll_scan);
		retrieveLl = (LinearLayout) findViewById(R.id.id_ll_retrieve);
		checkLl = (LinearLayout) findViewById(R.id.id_ll_check);
		settingsLl = (LinearLayout) findViewById(R.id.id_ll_settings);

		scanTv = (TextView) findViewById(R.id.id_tv_scan);
		retrieveTv = (TextView) findViewById(R.id.id_tv_retrieve);
		checkTv = (TextView) findViewById(R.id.id_tv_check);
		settingsTv = (TextView) findViewById(R.id.id_tv_settings);

		scanIv = (ImageView) findViewById(R.id.id_iv_scan);
		retrieveIv = (ImageView) findViewById(R.id.id_iv_retrieve);
		checkIv = (ImageView) findViewById(R.id.id_iv_check);
		settingsIv = (ImageView) findViewById(R.id.id_iv_settings);

		mViewPager = (ViewPager) findViewById(R.id.viewPager);

		mTopBar = (TopBar) findViewById(R.id.topbar);
		mTopBar.setTitle("资产扫描");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.id_ll_scan:

			mViewPager.setCurrentItem(0);

			break;
		case R.id.id_ll_retrieve:

			mViewPager.setCurrentItem(1);

			break;
		case R.id.id_ll_check:

			mViewPager.setCurrentItem(2);

			break;
		case R.id.id_ll_settings:

			mViewPager.setCurrentItem(3);

			break;
		default:
			break;
		}

	}
	
	@Override
	public void onBackPressed() {
        ActivityCollector.exitApp();
	}
}
