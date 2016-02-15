package com.example.assertmanager.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assertmanager.adapter.SimpleAdapter;
import com.example.assertmanager.adapter.ZcAdapter;
import com.example.assertmanager.adapter.TreeViewadapter.OnTreeNodeClickListener;
import com.example.assertmanager.application.MyApplication;
import com.example.assertmanager.db.DBManager;
import com.example.assertmanager.model.BaofeiItemBean;
import com.example.assertmanager.model.CheckItemBean;
import com.example.assertmanager.model.ZcBean;
import com.example.assertmanager.util.BaseActivity;
import com.example.assertmanager.util.Constant;
import com.example.assertmanager.util.Node;
import com.example.assertmanager.util.OnQueryCallbackListener;
import com.example.assertmanager.view.LoadListView;
import com.example.assertmanager.view.LoadListView.ILoadListener;
import com.example.assertmanager.view.SelectionBar;
import com.example.assertmanager.view.SelectionBar.OnSelected;
import com.example.assertmanager.view.TopBar;
import com.example.assertmanager.view.TopBar.OnLeftClickListener;
import com.example.myqr_codescan.R;

/**
 * 检索主界面
 * 
 * @author Xingfeng
 *
 */
public class RetrieveMainActivity extends BaseActivity implements
		OnClickListener {

	private SelectionBar selectionBar;

	private String zcBh;

	private String zcMc;

	private String zcGgxh;

	private String zcKm;

	private String zcSybm;

	private String zcBgr;

	private String zcGlbm;

	private String zcZy;

	private ProgressDialog progressDialog;

	private TextView totolNumTv;

	private TextView showNumTv;

	private LoadListView listView;

	private ZcAdapter adapter;

	private List<ZcBean> mDatas;

	private List<ZcBean> showDatas;

	private final int SHOW_NUM = 20;

	private QueryZcTask queryZcTask;

	private int baofeiFlag=0;//默认在用
	
	// 初始的科目名称
	private String initKmmc;
	// 初始的部门名称
	private String initBmmc;

	private ExecutorService exec = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrieve_main);

		exec = Executors.newCachedThreadPool();

		initDatas();

		initViews();

		initListeners();

		queryData();

	}

	private View rootView;

	private void initListeners() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent toZcMx = new Intent(RetrieveMainActivity.this,
						ZcMxActivity.class);
				ZcBean bean = mDatas.get(position);
				toZcMx.putExtra(Constant.ZC_BEAN_KEY, bean);
				RetrieveMainActivity.this.startActivity(toZcMx);

			}
		});

		listView.setInterface(new ILoadListener() {

			@Override
			public void onLoad() {

				if (mDatas.size() == showDatas.size()) {
					listView.loadComplete();
					Toast.makeText(RetrieveMainActivity.this, "已显示全部资产!!!",
							Toast.LENGTH_SHORT).show();
				} else {
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

				}
			}
		});

		selectionBar.setSelectedListener(new OnSelected() {

			@Override
			public void onKmSelect(String kmmc) {

				zcMc = kmmc;
				queryData();

			}

			@Override
			public void onFlagSelect(View v) {

				try {
					if (rootView == null)
						rootView = LayoutInflater.from(
								RetrieveMainActivity.this).inflate(
								R.layout.mutiplt_lv_layout, null);
					ListView listView = (ListView) rootView
							.findViewById(R.id.mutiple_lv);

					List<BaofeiItemBean> baifeiItems = new ArrayList<BaofeiItemBean>();
					baifeiItems.add(new BaofeiItemBean(1, 0, "在用"));
					baifeiItems.add(new BaofeiItemBean(2, 0, "部分报废"));
					baifeiItems.add(new BaofeiItemBean(3, 0, "全部报废"));

					SimpleAdapter<CheckItemBean> adapter = new SimpleAdapter<CheckItemBean>(
							listView, RetrieveMainActivity.this, baifeiItems, 2);
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

                            baofeiFlag=position;
							popupWindow.dismiss();
							queryData();

						}
					});
				} catch (IllegalAccessException e) {
					Toast.makeText(RetrieveMainActivity.this, "显示失败!!!",
							Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onBmSelect(String bmmc) {
				zcSybm = bmmc;
				queryData();
			}

			@Override
			public void onAllSelect() {

				zcSybm = initBmmc;
				zcMc = initKmmc;
				baofeiFlag=0;
				queryData();

			}
		});
	}

	private void queryData() {

		showDialog();

		queryZcTask = new QueryZcTask(zcBh, zcMc, zcGgxh, zcKm, zcSybm, zcBgr,
				zcGlbm, zcZy,baofeiFlag);
		queryZcTask.setListener(new OnQueryCallbackListener() {

			@Override
			public void onSuccess(final List<ZcBean> results) {

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						handleDatas(results);

						closeDialog();
					}

				});

			}

			@Override
			public void onError(Exception e) {

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						totolNumTv.setText("共0件");
						showNumTv.setText("已显示0件");
					}
				});

			}
		});

		exec.submit(queryZcTask);

	}

	/**
	 * 处理数据
	 * 
	 * @param results
	 *            数据集
	 */
	private void handleDatas(final List<ZcBean> results) {
		mDatas = results;

		if (showDatas == null)
			showDatas = new ArrayList<ZcBean>();
		else
			showDatas.clear();

		initShowDatas();

		// 分组,重新调整mDatas的位置
		// groupDatas();

		adapter = new ZcAdapter(RetrieveMainActivity.this, -1, mDatas);
		listView.setAdapter(adapter);

		totolNumTv.setText("共" + mDatas.size() + "件");
	}

	/*
	 * private void groupDatas() {
	 * 
	 * String group = topBarSp.getTitle().toString();
	 * 
	 * if (group.equals("按科目分组")) {
	 * 
	 * Collections.sort(showDatas, new Comparator<ZcBean>() {
	 * 
	 * @Override public int compare(ZcBean lhs, ZcBean rhs) {
	 * 
	 * int kmLhs = Integer.parseInt(lhs.getKm()); int kmRhs =
	 * Integer.parseInt(rhs.getKm());
	 * 
	 * return kmLhs < kmRhs ? -1 : kmLhs == kmRhs ? 0 : 1; } });
	 * 
	 * } else if (group.equals("按管理部门分组")) {
	 * 
	 * Collections.sort(showDatas, new Comparator<ZcBean>() {
	 * 
	 * @Override public int compare(ZcBean lhs, ZcBean rhs) {
	 * 
	 * String glbmLhs = lhs.getMxxglbm(); String glbmRhs = rhs.getMxxglbm();
	 * 
	 * return glbmLhs.compareTo(glbmRhs); } });
	 * 
	 * } else if (group.equals("按使用部门分组")) {
	 * 
	 * Collections.sort(showDatas, new Comparator<ZcBean>() {
	 * 
	 * @Override public int compare(ZcBean lhs, ZcBean rhs) {
	 * 
	 * String sybmLhs = lhs.getMxsybm(); String sybmRhs = rhs.getMxsybm();
	 * 
	 * return sybmLhs.compareTo(sybmRhs); } });
	 * 
	 * } }
	 */

	private void initShowDatas() {
		if ((mDatas.size() - showDatas.size()) >= SHOW_NUM) {
			int originSize = showDatas.size();
			for (int i = originSize; i < originSize + SHOW_NUM; i++)
				showDatas.add(mDatas.get(i));
		} else {
			for (int i = showDatas.size(); i < mDatas.size(); i++)
				showDatas.add(mDatas.get(i));
		}

		showNumTv.setText("已显示" + showDatas.size() + "件");

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

		zcBh = intent.getStringExtra(Constant.ZC_BH);
		zcMc = intent.getStringExtra(Constant.ZC_MC);
		zcGgxh = intent.getStringExtra(Constant.ZC_GGXH);
		zcKm = intent.getStringExtra(Constant.ZC_KM);
		zcSybm = intent.getStringExtra(Constant.ZC_SYBM);
		zcBgr = intent.getStringExtra(Constant.ZC_BGR);
		zcGlbm = intent.getStringExtra(Constant.ZC_GLBM);
		zcZy = intent.getStringExtra(Constant.ZC_ZY);

		initBmmc = zcSybm;
		initKmmc = zcKm;

	}

	@SuppressWarnings("unchecked")
	private void initViews() {

		selectionBar = (SelectionBar) findViewById(R.id.selectionbar);
		selectionBar.setBmmc(zcSybm);
		selectionBar.setKmmc(zcKm);

		totolNumTv = (TextView) findViewById(R.id.totol_num);
		showNumTv = (TextView) findViewById(R.id.show_num);
		listView = (LoadListView) findViewById(R.id.listView);

		TopBar top = (TopBar) findViewById(R.id.topbar);
		top.setTitle("资产检索情况");
		top.showLeftIv();
		top.setLeftListener(new OnLeftClickListener() {

			@Override
			public void onLeftClick(View v) {

				finish();

			}
		});

	}

	// 双击顶部，listview选中第一项
	private long lastClickTime;

	@Override
	public void onClick(View v) {

		if (System.currentTimeMillis() - lastClickTime > 1000) {
			lastClickTime = System.currentTimeMillis();
		} else {

			listView.setSelection(0);

		}

	}

	class QueryZcTask implements Runnable {

		private String zcBh;
		private String zcMc;
		private String zcGgxh;
		private String zcKm;
		private String zcSybm;
		private String zcBgr;
		private String zcGlbm;
		private String zcZy;
		
		//报废标志，0表示在用，1表示部门在用，2表示全部报废
        private int baofeiFlag;
		
		private OnQueryCallbackListener listener;

		public void setListener(OnQueryCallbackListener listener) {
			this.listener = listener;
		}

		public QueryZcTask(String zcBh, String zcMc, String zcGgxh,
				String zcKm, String zcSybm, String zcBgr, String zcGlbm,
				String zcZy,int baofeiFlag) {
			super();
			this.zcBh = zcBh;
			this.zcMc = zcMc;
			this.zcGgxh = zcGgxh;
			this.zcKm = zcKm;
			this.zcSybm = zcSybm;
			this.zcBgr = zcBgr;
			this.zcGlbm = zcGlbm;
			this.zcZy = zcZy;
			this.baofeiFlag=baofeiFlag;
			
		}

		public void run() {

			List<ZcBean> results = DBManager.getInstance().query(zcBh, zcMc,
					zcGgxh, zcKm, zcSybm, zcBgr, zcGlbm, zcZy,baofeiFlag);

			if (results != null && listener != null) {
				listener.onSuccess(results);
			}

			if (results == null && listener != null) {
				listener.onError(new NullPointerException("资产检索结果为null"));
			}

		}

	}

}
