package com.example.assertmanager.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.assertmanager.model.BmBean;
import com.example.assertmanager.model.KmBean;
import com.example.assertmanager.util.Constant;
import com.example.assertmanager.view.MutipleSpnnier;
import com.example.myqr_codescan.R;

/**
 * 检索Fragment
 * 
 * @author Xingfeng
 *
 */
public class RetrieveFragment extends Fragment implements OnClickListener {

	/**
	 * 资产编号
	 */
	private EditText zcBhEt;

	/**
	 * 资产名称
	 */
	private EditText zcMcEt;

	/**
	 * 资产规格型号
	 */
	private EditText zcGgxhEt;

	/**
	 * 资产科目Spinner
	 */
	private MutipleSpnnier<KmBean> zcKmSp;

	/**
	 * 使用部门Spinner
	 */
	private MutipleSpnnier<BmBean> zcSybmSp;

	/**
	 * 资产保管人
	 */
	private EditText zcBgrEt;
	/**
	 * 管理部门Spinner
	 */
	private MutipleSpnnier<BmBean> zcGlbmSp;

	/**
	 * 资产摘要
	 */
	private EditText zcZyEt;

	/**
	 * 检索按钮
	 */
	private Button retrieveBtn;

	/**
	 * 清空按钮
	 */
	private Button clearBtn;

	/**
	 * 父activity
	 */
	private Activity parentActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parentActivity = activity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_retrieve, container,
				false);

		zcBhEt = (EditText) view.findViewById(R.id.zcbm_et);
		zcMcEt = (EditText) view.findViewById(R.id.zcmc_et);
		zcGgxhEt = (EditText) view.findViewById(R.id.zcggxh_et);
		zcKmSp = (MutipleSpnnier<KmBean>) view
				.findViewById(R.id.assert_type_sp);
		zcSybmSp = (MutipleSpnnier<BmBean>) view
				.findViewById(R.id.department_use_sp);
		zcBgrEt = (EditText) view.findViewById(R.id.zcbgr_et);
		zcGlbmSp = (MutipleSpnnier<BmBean>) view
				.findViewById(R.id.department_manager_sp);
		zcZyEt = (EditText) view.findViewById(R.id.zczy_et);

		retrieveBtn = (Button) view.findViewById(R.id.retrieve_btn);
		clearBtn = (Button) view.findViewById(R.id.clear_btn);

		initDatas();

		retrieveBtn.setOnClickListener(this);
		clearBtn.setOnClickListener(this);

		return view;
	}

	private void initDatas() {
		zcGlbmSp.setType(new BmBean(0, 0, null));
		zcSybmSp.setType(new BmBean(0, 0, null));
		zcKmSp.setType(new KmBean(0, 0, null));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.retrieve_btn:

			Intent intent = new Intent(parentActivity,
					RetrieveMainActivity.class);

			intent.putExtra(Constant.ZC_BH, zcBhEt.getText().toString());
			intent.putExtra(Constant.ZC_MC, zcMcEt.getText().toString());
			intent.putExtra(Constant.ZC_GGXH, zcGgxhEt.getText().toString());
			intent.putExtra(Constant.ZC_KM, zcKmSp.getTitle());
			intent.putExtra(Constant.ZC_SYBM, zcSybmSp.getTitle());
			intent.putExtra(Constant.ZC_BGR, zcBgrEt.getText().toString());
			intent.putExtra(Constant.ZC_GLBM, zcGlbmSp.getTitle());
			intent.putExtra(Constant.ZC_ZY, zcZyEt.getText().toString());

			startActivity(intent);

			break;

		case R.id.clear_btn:

			zcBhEt.setText("");
			zcMcEt.setText("");
			zcGgxhEt.setText("");
			zcKmSp.setTitle("");
			zcSybmSp.setTitle("");
			zcBgrEt.setText("");
			zcGlbmSp.setTitle("");
			zcZyEt.setText("");

			break;
		default:
			break;
		}

	}

}
