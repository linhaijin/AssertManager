package com.example.assertmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assertmanager.util.BaseActivity;
import com.example.assertmanager.view.TopBar;
import com.example.assertmanager.view.TopBar.OnLeftClickListener;
import com.example.myqr_codescan.R;

/**
 * 手工输入的界面
 * 
 * @author Xingfeng
 *
 */
public class ManualActivity extends BaseActivity implements OnClickListener {

	private EditText zcBhEt;

	private Button retrieveBtn;

	private Button clearBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manual);

		initViews();

		initListeners();
	}

	private void initListeners() {

		retrieveBtn.setOnClickListener(this);
		clearBtn.setOnClickListener(this);

	}

	private void initViews() {

		zcBhEt = (EditText) findViewById(R.id.zc_bh_et);
		retrieveBtn = (Button) findViewById(R.id.retrieve_btn);
		clearBtn = (Button) findViewById(R.id.clear_btn);

		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		topBar.setTitle("手工定位清查");
		topBar.showLeftIv();
		topBar.setLeftListener(new OnLeftClickListener() {

			@Override
			public void onLeftClick(View v) {

				finish();

			}
		});

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.retrieve_btn:

			String input = zcBhEt.getText().toString();

			if (!TextUtils.isEmpty(input)) {

				if (input.length() != 18) {

					Toast.makeText(ManualActivity.this, "资产编号长度不正确！",
							Toast.LENGTH_SHORT).show();

				} else {
					// 进入资产详细界面
					Intent toZcMxActivity = new Intent(this,
							CheckZcMxActivity.class);
					toZcMxActivity.putExtra("zcbh", input);
					startActivity(toZcMxActivity);

				}

			}else{
				
				Toast.makeText(ManualActivity.this, "资产编号不可以为空!", Toast.LENGTH_SHORT).show();
				
			}

			break;

		case R.id.clear_btn:

			zcBhEt.setText("");

			break;
		default:
			break;
		}

	}

}
