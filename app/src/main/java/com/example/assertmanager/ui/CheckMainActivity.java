package com.example.assertmanager.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assertmanager.adapter.SimpleAdapter;
import com.example.assertmanager.adapter.TreeViewadapter.OnTreeNodeClickListener;
import com.example.assertmanager.adapter.ZcAdapter;
import com.example.assertmanager.application.MyApplication;
import com.example.assertmanager.db.DBManager;
import com.example.assertmanager.model.CheckItemBean;
import com.example.assertmanager.model.ZcBean;
import com.example.assertmanager.util.BaseActivity;
import com.example.assertmanager.util.Constant;
import com.example.assertmanager.util.Node;
import com.example.assertmanager.view.LoadListView;
import com.example.assertmanager.view.LoadListView.ILoadListener;
import com.example.assertmanager.view.SelectionBar;
import com.example.assertmanager.view.SelectionBar.OnSelected;
import com.example.assertmanager.view.TopBar;
import com.example.assertmanager.view.TopBar.OnLeftClickListener;
import com.example.myqr_codescan.R;

/**
 * 清查主界面
 * 
 * @author Xingfeng
 *
 */
public class CheckMainActivity extends BaseActivity implements OnClickListener {

	public static String sybmStr;
	private String zckmmc = "";// 资产科目名称

	private ProgressDialog progressDialog;

	/**
	 * 共有物品的件数
	 */
	private TextView totalNumTv;

	/**
	 * 符合条件数目的件数
	 */
	private TextView macthNumTv;;

	/**
	 * 筛选条件提示
	 */
	private TextView filterCondionPromtTv;

	/**
	 * 筛选条件内容
	 */
	private TextView filterConditionTv;

	/**
	 * 清查数量
	 */
	private TextView checkNumTv;

	private SelectionBar selectionBar;

	private LoadListView listView;

	private ZcAdapter adapter;

	private List<ZcBean> mDatas;

	/**
	 * 显示的数据，每次显示20个
	 */
	private List<ZcBean> showDatas;

	private final int SHOW_NUM = 20;

	/**
	 * 射频Button
	 */
	private Button shepinBtn;

	/**
	 * 条码Button
	 */
	private Button tiaomaBtn;

	/**
	 * 激光条码
	 */
	private Button laserBtn;

	/**
	 * 手工Button
	 */
	private Button shougongBtn;

	/**
	 * 清查标志，0表示未清查，1表示清查，2表示全部
	 */
	private int checkFlag = 2;

	/**
	 * 是否执行过查找清查数量的操作
	 */
	private boolean hasCheckedNum = false;

	/**
	 * 清查数量
	 */
	private int checkNum = 0;

	/**
	 * 选择栏选择的下标
	 */
	private int filterIndex = 0;

	/**
	 * 选择条件内容
	 */
	private String filterContent;

	/**
	 * 清查日期
	 */
	public static String qcrq = null;

	private boolean isLoadCompleted = false;// 加载完所有数据的标志

	private View rootView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_main);

		initDatas();

		initViews();

		initListeners();

		queryData(zckmmc, sybmStr, checkFlag);// 默认清查符合使用部门条件的所有科目的、清查和未清查所有的资产

	}

	private void initListeners() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent toZcMx = new Intent(CheckMainActivity.this,
						ZcMxActivity.class);
				ZcBean bean = showDatas.get(position);
				toZcMx.putExtra(Constant.ZC_BEAN_KEY, bean);
				CheckMainActivity.this.startActivity(toZcMx);

			}
		});

		listView.setInterface(new ILoadListener() {

			@Override
			public void onLoad() {

				if (!isLoadCompleted) {
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {

							int top = showDatas.size();
							initShowDatas();
							int bottom = showDatas.size();
							int selectionPosition = bottom > top ? top
									: top - 1;
							adapter.notifyDataSetChanged();
							listView.setSelection(selectionPosition);
							listView.loadComplete();

						}
					}, 2000);
				} else {
					listView.loadComplete();
					Toast.makeText(CheckMainActivity.this, "已显示全部资产",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		selectionBar.setSelectedListener(new OnSelected() {

			@Override
			public void onKmSelect(String kmmc) {

				zckmmc = kmmc;
				queryData(zckmmc, sybmStr, checkFlag);

			}

			@SuppressLint("InflateParams")
			@Override
			public void onFlagSelect(View v) {

				try {
					if (rootView == null)
						rootView = LayoutInflater.from(CheckMainActivity.this)
								.inflate(R.layout.mutiplt_lv_layout, null);
					ListView listView = (ListView) rootView
							.findViewById(R.id.mutiple_lv);       
					
					List<CheckItemBean> checkItems = new ArrayList<CheckItemBean>();
					checkItems.add(new CheckItemBean(1, 0, "未清查"));
					checkItems.add(new CheckItemBean(2, 9, "已清查"));
					checkItems.add(new CheckItemBean(3, 0, "全部"));

					SimpleAdapter<CheckItemBean> adapter = new SimpleAdapter<CheckItemBean>(
							listView, CheckMainActivity.this, checkItems, 2);
					
					listView.setAdapter(adapter);
					final PopupWindow popupWindow = new PopupWindow(rootView,
							MyApplication.popWindowWidth,
							MyApplication.popWindowHeight);
					popupWindow.setFocusable(true);
					popupWindow.setBackgroundDrawable(new BitmapDrawable());
					popupWindow.setOutsideTouchable(true);
					popupWindow.update();
					popupWindow.showAsDropDown(v);
					adapter.setOnTreeNodeClickListeener(new OnTreeNodeClickListener() {

						@Override
						public void onClick(Node node, int position, int type) {
                               						
							checkFlag = position;
							popupWindow.dismiss();
							queryData(zckmmc, sybmStr, checkFlag);

						}
					});
				} catch (IllegalAccessException e) {
					Toast.makeText(CheckMainActivity.this, "显示失败!!!",
							Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onBmSelect(String bmmc) {

			}

			@Override
			public void onAllSelect() {

				zckmmc = "";
				checkFlag = 2;
				queryData(zckmmc, sybmStr, checkFlag);

			}
		});

		shepinBtn.setOnClickListener(this);
		tiaomaBtn.setOnClickListener(this);
		laserBtn.setOnClickListener(this);
		shougongBtn.setOnClickListener(this);
	}

	/**
	 * 查询资产
	 * 
	 * @param kmmc
	 *            科目名称
	 * @param sybmmc
	 *            使用部门名称
	 * @param flag
	 *            清查标志0表示未清查，1表示清查，2表示全部
	 */
	private void queryData(final String kmmc, final String sybmmc,
			final int flag) {

		showDialog();

		new Thread(new Runnable() {

			@Override
			public void run() {

				mDatas = DBManager.getInstance().query(kmmc, sybmmc, flag);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						if (showDatas == null)
							showDatas = new ArrayList<ZcBean>();
						else
							showDatas.clear();

						initShowDatas();

						adapter = new ZcAdapter(CheckMainActivity.this, -1,
								showDatas);

						listView.setAdapter(adapter);

						setTextViewContent();

						closeDialog();
					}

				});

			}
		}).start();

	}

	/**
	 * 为几个TextView设置值
	 */
	private void setTextViewContent() {

		if (!hasCheckedNum) {

			totalNumTv.setText(mDatas.size() + "件");

			for (ZcBean bean : mDatas) {
				if (bean.getQcsl() == 1) {
					++checkNum;
				}
			}

			checkNumTv.setText(checkNum + "件");

			hasCheckedNum = true;
		}

		macthNumTv.setText(mDatas.size() + "件");

		switch (filterIndex) {
		// 全部
		case 0:

			filterCondionPromtTv.setText("使用部门:");
			filterConditionTv.setText(filterContent);

			break;

		// 科目
		case 1:

			filterCondionPromtTv.setText("科目:");
			filterConditionTv.setText(filterContent);

			break;

		// 清查
		case 4:

			filterCondionPromtTv.setText("使用部门:");
			filterConditionTv.setText(sybmStr);

			break;

		default:
			break;
		}

	}

	private void initShowDatas() {
		if ((mDatas.size() - showDatas.size()) >= SHOW_NUM) {
			int originSize = showDatas.size();
			for (int i = originSize; i < originSize + SHOW_NUM; i++)
				showDatas.add(mDatas.get(i));
		} else {
			for (int i = showDatas.size(); i < mDatas.size(); i++)
				showDatas.add(mDatas.get(i));

		}

		if (mDatas.size() == showDatas.size()) {
			isLoadCompleted = true;
		}
	}

	private void showDialog() {

		if (progressDialog == null)
			progressDialog = new ProgressDialog(this);

		progressDialog.setMessage("正在查询资产详细信息...");
		progressDialog.show();

	}

	private void closeDialog() {
		if (progressDialog != null)
			progressDialog.cancel();
	}

	private void initDatas() {

		Intent intent = getIntent();
		sybmStr = intent.getStringExtra(Constant.ZC_SYBM);

		filterContent = sybmStr;

	}

	@SuppressWarnings("unchecked")
	private void initViews() {

		macthNumTv = (TextView) findViewById(R.id.matct_condition_num_tv);
		totalNumTv = (TextView) findViewById(R.id.totol_num_tv);
		filterCondionPromtTv = (TextView) findViewById(R.id.filter_condition_promt);
		filterConditionTv = (TextView) findViewById(R.id.filter_condition_tv);
		checkNumTv = (TextView) findViewById(R.id.check_num_tv);
		listView = (LoadListView) findViewById(R.id.listView);

		TopBar top = (TopBar) findViewById(R.id.topbar);
		top.setTitle("资产清查情况");
		top.showLeftIv();
		top.setLeftListener(new OnLeftClickListener() {

			@Override
			public void onLeftClick(View v) {

				finish();

			}
		});
		/*top.showRightIv();
		top.setRightImageResource(R.drawable.icon_overflow);
		top.setRightListener(new OnRightClcikListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onRightClick(View v) {

				// 弹出PopupWindow供用户选择

				List<String> groupList = new ArrayList<String>();
				groupList.add("按科目分组");
				groupList.add("按部门分组");

				ListView listView = new ListView(CheckMainActivity.this);
				GroupItemAdapter adapter = new GroupItemAdapter(
						CheckMainActivity.this, -1, groupList);
				listView.setAdapter(adapter);
				final PopupWindow popupWindow = new PopupWindow(listView, 500,
						300);
				listView.setDivider(getResources().getDrawable(
						R.drawable.divider));
				popupWindow.setFocusable(true);
				popupWindow.setBackgroundDrawable(new BitmapDrawable());
				popupWindow.setOutsideTouchable(true);
				popupWindow.showAsDropDown(v);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						// TextView tv = (TextView) view
						// .findViewById(R.id.group_item_tv);
						// tv.setTextColor(Color.parseColor("#8F6225"));
						//
						// checkFlag = 2 - position;
						// queryData();
						popupWindow.dismiss();
					}
				});

			}
		});*/
		top.setOnClickListener(this);

		selectionBar = (SelectionBar) findViewById(R.id.selectionbar);
		selectionBar.setKmmc("");
		selectionBar.setBmmc(sybmStr);

		shepinBtn = (Button) findViewById(R.id.shepin_btn);
		tiaomaBtn = (Button) findViewById(R.id.tiaoma_btn);
		laserBtn = (Button) findViewById(R.id.laser_btn);
		shougongBtn = (Button) findViewById(R.id.shougong_btn);

	}

	// 双击顶部，listview选中第一项
	private long lastClickTime;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.topbar:

			if (System.currentTimeMillis() - lastClickTime > 1000) {
				lastClickTime = System.currentTimeMillis();
			} else {

				listView.setSelection(0);

			}

			break;
		// 射频清查
		case R.id.shepin_btn:

			Intent toShepin = new Intent(this, ShepinActivity.class);
			toShepin.putExtra("type", Constant.CHECK);// 清查标志
			startActivity(toShepin);

			break;
		// 条码清查
		case R.id.tiaoma_btn:

			Intent toTiaoma = new Intent(this, MipcaActivityCapture.class);
			toTiaoma.putExtra("type", Constant.CHECK);// 清查标志
			startActivity(toTiaoma);

			break;
		// 激光清查
		case R.id.laser_btn:

			Intent toLaser = new Intent(this, LaserCheckActivity.class);
			toLaser.putExtra("type", Constant.CHECK);// 清查标志
			startActivity(toLaser);

			break;
		// 手工清查
		case R.id.shougong_btn:

			startActivity(new Intent(this, ManualActivity.class));

			break;

		default:
			break;
		}
	}

}
