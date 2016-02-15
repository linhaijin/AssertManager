package com.example.assertmanager.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.assertmanager.util.BaseActivity;
import com.example.assertmanager.util.Constant;
import com.example.assertmanager.view.TopBar;
import com.example.assertmanager.view.TopBar.OnLeftClickListener;
import com.example.myqr_codescan.R;

/**
 * 检索结果显示界面
 * 
 * @author Xingfeng
 *
 */
public class RetrieveResultShowActivity extends BaseActivity {

	private ListView mResultLv;

	/**
	 * 继续扫描按钮
	 */
	private Button goOnScanBtn;

	/**
	 * 资产编号列表
	 */
	private ArrayList<String> mZcBhs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrieve_result_show);

		initViews();
		initDatas();
	}

	private void initDatas() {

		mZcBhs = (ArrayList<String>) getIntent().getStringArrayListExtra(
				Constant.RETRIEVE_RESULT_LIST);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mZcBhs);
		mResultLv.setAdapter(adapter);

	}

	private void initViews() {

		TopBar topbar = (TopBar) findViewById(R.id.topbar);
		topbar.setTitle("检索结果");
		topbar.showLeftIv();
		topbar.setLeftListener(new OnLeftClickListener() {

			@Override
			public void onLeftClick(View v) {

				finish();

			}
		});

		mResultLv = (ListView) findViewById(R.id.check_result_lv);
		goOnScanBtn = (Button) findViewById(R.id.go_on_scan_btn);
		goOnScanBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});
	}

}
