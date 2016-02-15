package com.example.assertmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.assertmanager.db.DBManager;
import com.example.assertmanager.model.ChangeZcBean;
import com.example.assertmanager.model.ZcBean;
import com.example.assertmanager.util.BaseActivity;
import com.example.assertmanager.util.Constant;
import com.example.assertmanager.util.DBLogUtil;
import com.example.assertmanager.view.TopBar;
import com.example.assertmanager.view.TopBar.OnLeftClickListener;
import com.example.assertmanager.view.TopBar.OnRightClcikListener;
import com.example.myqr_codescan.R;

public class ZcMxActivity extends BaseActivity {

	private ZcBean zcBean;

	/**
	 * 报废
	 */
	private CheckBox baofeiBox;

	/**
	 * 调整
	 */
	private CheckBox tiaozhengBox;

	/**
	 * 条码
	 */
	private CheckBox tiaomaBox;

	// 改变标志，0为报废、1为调整、2为条码、3为图片
	private int[] changeFlag = new int[4];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zc_mx);

		initDatas();

		initViews();

		initListeners();
	}

	private void initListeners() {

		baofeiBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				// 只有在没有报废的情况下选择了报废，或者是报废的情况下选择了没有报废
				int baofei = zcBean.getQcbfbz();

				if (baofei == 0 && isChecked)
					changeFlag[0] = 1;

				if (baofei == 1 && !isChecked)
					changeFlag[0] = 1;

				zcBean.setQcbfbz(isChecked ? 1 : 0);
			}
		});
		tiaozhengBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				int tiaozheng = zcBean.getQctzbz();

				if (tiaozheng == 0 && isChecked)
					changeFlag[1] = 1;

				if (tiaozheng == 1 && !isChecked)
					changeFlag[1] = 1;

			}
		});
		tiaomaBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				int tiaoma = zcBean.getQctmbz();

				if (tiaoma == 0 && isChecked)
					changeFlag[2] = 1;

				if (tiaoma == 1 && !isChecked)
					changeFlag[2] = 1;

			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// 更新记录
		new Thread(new Runnable() {

			@Override
			public void run() {

				// 四个标志中有一个为1,则需要进行更新
				if ((changeFlag[0] | changeFlag[1] | changeFlag[2] | changeFlag[3]) == 1) {

					ChangeZcBean changeBean = new ChangeZcBean();
					if (changeFlag[0] == 1) {

						zcBean.setQcbfbz(1 | zcBean.getQcbfbz());
					}

					if (changeFlag[1] == 1) {

						zcBean.setQctzbz(1 | zcBean.getQctzbz());
					}

					if (changeFlag[2] == 1) {

						zcBean.setQctmbz(1);
					}

					if (changeFlag[3] == 1) {
						zcBean.setQctpbz(1 | zcBean.getQctpbz());
					}

					// 更新数据库
					DBManager.getInstance().update(zcBean);

					changeBean.setMxbh(zcBean.getMxbh());
					changeBean.setQcbfbz(zcBean.getQcbfbz());
					changeBean.setQcsl(zcBean.getQcsl());
					changeBean.setQctmbz(zcBean.getQctmbz());
					changeBean.setQctpbz(zcBean.getQctpbz());
					changeBean.setQctzbz(zcBean.getQctzbz());
					// 将记录记录在日志中
					DBLogUtil.getInstance().write(changeBean);

				}

			}
		}).start();

	}

	private void initViews() {

		TopBar topBar = (TopBar) findViewById(R.id.topbar);
		topBar.setTitle("资产详细信息");
		topBar.showLeftIv();
		topBar.setLeftListener(new OnLeftClickListener() {

			@Override
			public void onLeftClick(View v) {

				finish();

			}
		});
		topBar.showRightIv();
		topBar.setRightImageResource(R.drawable.ic_action_camera);
		topBar.setRightListener(new OnRightClcikListener() {

			@Override
			public void onRightClick(View v) {

				Intent imageShowIntent = new Intent(ZcMxActivity.this,
						ImageShowActivity.class);
				imageShowIntent.putExtra(Constant.ZC_BEAN_KEY, zcBean);
				startActivityForResult(imageShowIntent, 0);

			}

		});

		TextView zcbhTv = (TextView) findViewById(R.id.zc_bh_tv);
		String bh=zcBean.getMxbh();
		zcbhTv.setText(zcBean.getMxbh().subSequence(bh.length()-18,
				bh.length()));

		TextView zcKmTv = (TextView) findViewById(R.id.zc_km_tv);
		zcKmTv.setText(zcBean.getKm());

		TextView zcMcTv = (TextView) findViewById(R.id.zc_mc_tv);
		zcMcTv.setText(zcBean.getMc());

		TextView zcGgxhTv = (TextView) findViewById(R.id.zc_ggxh_tv);
		zcGgxhTv.setText(zcBean.getGgxh());

		// TextView zcMxslTv = (TextView) findViewById(R.id.zc_mxsl_tv);
		// zcMxslTv.setText(zcBean.getMxsl() + "");

		TextView zcDjTv = (TextView) findViewById(R.id.zc_dj_tv);
		zcDjTv.setText(zcBean.getDj() + "");

		// TextView zcMxjeTv = (TextView) findViewById(R.id.zc_mxje_tv);
		// zcMxjeTv.setText(zcBean.getMxje() + "");

		TextView zcMxxglbmTv = (TextView) findViewById(R.id.zc_mxxglbm_tv);
		zcMxxglbmTv.setText(zcBean.getMxxglbm());

		TextView zcsybmTv = (TextView) findViewById(R.id.zc_mxsybm_tv);
		zcsybmTv.setText(zcBean.getMxsybm());

		TextView zcbgrTv = (TextView) findViewById(R.id.zc_bgr_tv);
		zcbgrTv.setText(zcBean.getMxbgr());

		// TextView zcRqTv = (TextView) findViewById(R.id.zc_rq_tv);
		// zcRqTv.setText(zcBean.getRq());

		// TextView zcSynxTv = (TextView) findViewById(R.id.zc_synx_tv);
		// zcSynxTv.setText(zcBean.getMxsynx() + "");

		// TextView zcJsslTv = (TextView) findViewById(R.id.zc_jssl_tv);
		// zcJsslTv.setText(zcBean.getMxjssl() + "");

		// TextView zcJsjeTv = (TextView) findViewById(R.id.zc_jsje_tv);
		// zcJsjeTv.setText(zcBean.getMxjsje() + "");
		//
		// TextView zcXhTv = (TextView) findViewById(R.id.zc_xh_tv);
		// zcXhTv.setText(zcBean.getXh() + "");
		//
		// TextView zcTzdhTv = (TextView) findViewById(R.id.zc_tzdh_tv);
		// zcTzdhTv.setText(zcBean.getTzdh() + "");

		TextView zcZyTv = (TextView) findViewById(R.id.zc_zy_tv);
		zcZyTv.setText(zcBean.getZy());

		TextView zcSyzkTv = (TextView) findViewById(R.id.zc_syzk_tv);

		String zcSyzkMc = DBManager.getInstance().getSyzkMc(zcBean.getMxsyzk());
		if (zcSyzkMc != null)
			zcSyzkTv.setText(zcSyzkMc);

		// TextView zcRkbzTv = (TextView) findViewById(R.id.zc_rkbz_tv);
		// zcRkbzTv.setText(zcBean.getRzbz() + "");

		baofeiBox = (CheckBox) findViewById(R.id.zc_baofei_flag);
		baofeiBox.setChecked(zcBean.getQcbfbz() == 0 ? false : true);
		tiaozhengBox = (CheckBox) findViewById(R.id.zc_tiaozheng_flag);
		tiaozhengBox.setChecked(zcBean.getQctzbz() == 0 ? false : true);
		tiaomaBox = (CheckBox) findViewById(R.id.zc_tiaoma_flag);
		tiaomaBox.setChecked(zcBean.getQctmbz() == 0 ? false : true);

	}

	private void initDatas() {

		zcBean = (ZcBean) getIntent()
				.getSerializableExtra(Constant.ZC_BEAN_KEY);

	}

	/**
	 * 从ImageShow界面中处理是否重拍过照片
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO 将图片保存到“改变图片”目录中，并更新“图片”目录中的图片，记录日志文件
		if (requestCode == 0 && resultCode == RESULT_OK) {

			if (changeFlag[3] == 0)
				changeFlag[3] = intent.getIntExtra(
						Constant.PICTURE_CHANGE_FLAG, 0);

		}

	}
}
