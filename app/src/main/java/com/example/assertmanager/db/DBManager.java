package com.example.assertmanager.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.assertmanager.model.BmBean;
import com.example.assertmanager.model.KmBean;
import com.example.assertmanager.model.ZcBean;
import com.example.assertmanager.util.Constant;

public class DBManager {

	private static DBManager instance = null;

	private SQLiteDatabase db;

	/**
	 * 资产表
	 */
	private static final String TABLE_ZC = "t_zc";

	/**
	 * 部门表
	 */
	private static final String TABLE_BM = "t_bm";

	/**
	 * 获取方式表
	 */
	private static final String TABLE_HQFS = "t_hqfs";

	/**
	 * 科目表
	 */
	private static final String TABLE_KM = "t_km";

	/**
	 * 使用状况表
	 */
	private static final String TABLE_SYZK = "t_syzk";

	private DBManager() {
		// String dbPath = FindDbFileUtil.getDbPath();
		String dbPath = Constant.DB_PATH + Constant.DB_FILE_NAME;
		db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	public static DBManager getInstance() {

		if (instance == null)
			synchronized (DBManager.class) {
				if (instance == null)
					instance = new DBManager();

			}

		return instance;

	}

	/**
	 * 检索时查询,除了使用部门和管理部门必须选择外，其余为空时均为查询所有
	 * 
	 * @param zcbh
	 *            资产编号,对应mxbh字段
	 * @param zcmc
	 *            资产名称,对应mc字段
	 * @param zcggxh
	 *            规格型号，对应ggxh字段
	 * @param zckm
	 *            资产科目（使用的是名称），对应km字段，需将科目名称转化为科目代号
	 * @param zcsybm
	 *            资产使用部门，对应mxsybm字段
	 * @param zcbgr
	 *            资产保管人，对应mxbgr字段
	 * @param zcglbm
	 *            资产管理部门，对应mxxglbm字段
	 * @param zczy
	 *            资产摘要，对应zy字段
	 * 
	 * @return 符合条件的资产列表
	 */
	public List<ZcBean> query(String zcbh, String zcmc, String zcggxh,
			String zckm, String zcsybm, String zcbgr, String zcglbm, String zczy) {

		String sql = "select * from " + TABLE_ZC
				+ " where mxbh like ? and mc like ? and ggxh like ? "
				+ "and km like ? and mxsybm like ? and mxbgr like ? "
				+ "and mxxglbm like ? and zy like ?;";

		String[] selections = new String[8];

		selections[0] = "%" + zcbh + "%";

		selections[1] = "%" + zcmc + "%";

		selections[2] = "%" + zcggxh + "%";

		String kmbh = getKmbm(zckm);
		if (kmbh == null)
			kmbh = "16%";
		selections[3] = kmbh;
		selections[4] = "%" + zcsybm;
		selections[5] = "%" + zcbgr + "%";
		selections[6] = "%" + zcglbm;
		selections[7] = "%" + zczy + "%";

		Cursor cursor = db.rawQuery(sql, selections);

		if (cursor != null) {

			return fillListWithCursor(cursor);
		}

		return null;

	}

	/**
	 * 检索界面的查询接口
	 * 
	 * @param zcbh
	 * @param zcmc
	 * @param zcggxh
	 * @param zckm
	 * @param zcsybm
	 * @param zcbgr
	 * @param zcglbm
	 * @param zczy
	 * @param baofeiFlag
	 *            报废标志。0表示在用，1表示部门在用，2表示全部报废
	 * @return 数据集为空，返回null，不为空，返回数据集
	 */
	public List<ZcBean> query(String zcbh, String zcmc, String zcggxh,
			String zckm, String zcsybm, String zcbgr, String zcglbm,
			String zczy, int baofeiFlag) {

		List<ZcBean> result = query(zcbh, zcmc, zcggxh, zckm, zcsybm, zcbgr,
				zcglbm, zczy);

		if (result != null) {
			
			// 从结果中删选出符合结果的资产
			ZcBean bean=null;
			switch (baofeiFlag) {
			case 0:
                
				//遍历，不是在用的则删除
				for(int i=0;i<result.size();i++){
					bean=result.get(i);
					if(!(bean.getSl()==2*bean.getMxsl())){
						result.remove(i);
					}
					bean=null;
				}
				
				break;
			case 1:
                
				//遍历，不是部分在用的则删除
				for(int i=0;i<result.size();i++){
					bean=result.get(i);
					int temp=bean.getSl()-bean.getMxsl();
					if(!(temp>0&&temp<bean.getMxsl())){
						result.remove(i);
					}
					bean=null;
				}
				
				break;
			case 2:
                
				//遍历，不是全部报废的则删除
				for(int i=0;i<result.size();i++){
					bean=result.get(i);
					int temp=bean.getSl()-bean.getMxsl();
					if(!(temp==0)){
						result.remove(i);
					}
					bean=null;
				}
				
				break;
			default:
				break;
			}

		}

		return result;
	}

	/**
	 * 查询资产
	 * 
	 * @param zckm
	 *            资产科目
	 * @param sybmmc
	 *            使用部门名称
	 * @param checkFlag
	 *            清查标志，0表示未清查，1表示清查，2表示全部
	 * @return
	 */
	public List<ZcBean> query(String zckm, String sybmmc, int checkFlag) {

		List<ZcBean> resultList = new ArrayList<ZcBean>();

		String sql = "select * from " + TABLE_ZC
				+ " where km like ? and mxsybm like ?;";

		String selectionArgs1 = "";
		String selectionArgs2 = "%" + sybmmc;

		// 获取资产编号
		String kmbh = getKmbm(zckm);

		if (kmbh != null)
			selectionArgs1 = kmbh + "%";
		else
			selectionArgs1 = "16%";

		Cursor cursor = db.rawQuery(sql, new String[] { selectionArgs1,
				selectionArgs2 });

		if (cursor != null) {
			ZcBean bean;

			while (cursor.moveToNext()) {

				bean = new ZcBean();

				bean.setQcsl(cursor.getInt(cursor.getColumnIndex("qcsl")));

				// 选择清查或者未清查的
				if (checkFlag != 2) {

					if (bean.getQcsl() != checkFlag)
						continue;

				}

				bean.setId(cursor.getString(cursor.getColumnIndex("id")));
				bean.setMxbh(cursor.getString(cursor.getColumnIndex("mxbh")));
				bean.setMc(cursor.getString(cursor.getColumnIndex("mc")));
				bean.setRq(cursor.getString(cursor.getColumnIndex("rq")));
				bean.setMxsybm(cursor.getString(cursor.getColumnIndex("mxsybm")));
				bean.setGgxh(cursor.getString(cursor.getColumnIndex("ggxh")));
				bean.setDj(cursor.getInt(cursor.getColumnIndex("dj")));
				bean.setKm(cursor.getString(cursor.getColumnIndex("km")));
				bean.setMxbgr(cursor.getString(cursor.getColumnIndex("mxbgr")));
				bean.setMxje(cursor.getFloat(cursor.getColumnIndex("mxje")));
				bean.setMxjsje(cursor.getFloat(cursor.getColumnIndex("mxjsje")));
				bean.setMxjssl(cursor.getInt(cursor.getColumnIndex("mxjssl")));
				bean.setMxsl(cursor.getInt(cursor.getColumnIndex("mxsl")));
				bean.setSl(cursor.getInt(cursor.getColumnIndex("sl")));
				bean.setMxsynx(cursor.getInt(cursor.getColumnIndex("mxsynx")));
				bean.setMxxglbm(cursor.getString(cursor
						.getColumnIndex("mxxglbm")));
				bean.setQcrq(cursor.getString(cursor.getColumnIndex("qcrq")));
				bean.setQcbfbz(cursor.getInt(cursor.getColumnIndex("qcbfbz")));
				bean.setQctzbz(cursor.getInt(cursor.getColumnIndex("qctzbz")));
				bean.setQctmbz(cursor.getInt(cursor.getColumnIndex("qctmbz")));
				bean.setQctpbz(cursor.getInt(cursor.getColumnIndex("qctpbz")));
				bean.setRzbz(cursor.getInt(cursor.getColumnIndex("rzbz")));
				bean.setTzdh(cursor.getInt(cursor.getColumnIndex("tzdh")));
				bean.setXh(cursor.getInt(cursor.getColumnIndex("xh")));
				bean.setZy(cursor.getString(cursor.getColumnIndex("zy")));
				bean.setMxsyzk(cursor.getInt(cursor.getColumnIndex("mxsyzk")));
				resultList.add(bean);

				bean = null;

			}

		}

		return resultList;

	}

	/**
	 * 查询
	 * 
	 * @param glbm
	 *            管理部门
	 * @param kmmc
	 *            科目名称
	 * @param sybm
	 *            使用部门
	 * @param checkFlag
	 *            清查标志，0为未清查，1为已清查，2为全部
	 * @return
	 */
	public List<ZcBean> query(String glbm, String kmmc, String sybm,
			int checkFlag) {

		String sql = "select * from " + TABLE_ZC
				+ " where mxxglbm like ? and km like ? and mxsybm like ?;";

		String selectionArgs1 = "%" + glbm;
		String selectionArgs2 = "";
		String selectionArgs3 = "%" + sybm;

		// 获取资产编号
		String kmbh = getKmbm(kmmc);

		if (kmbh != null)
			selectionArgs2 = kmbh + "%";
		else
			kmbh = "16%";

		Cursor cursor = db.rawQuery(sql, new String[] { selectionArgs1,
				selectionArgs2, selectionArgs3 });

		if (cursor != null) {

			return fillListWithCursor(cursor);

		}

		return null;

	}

	/**
	 * 按照使用部门查询资产
	 * 
	 * @param sybm
	 *            使用部门
	 * @return
	 */
	public List<ZcBean> queryBySybm(String sybm) {

		String sql = "select * from " + TABLE_ZC + " where mxsybm like ?;";

		Cursor cursor = db.rawQuery(sql, new String[] { "%" + sybm });

		if (cursor != null) {

			return fillListWithCursor(cursor);

		}

		return null;

	}

	/**
	 * 将Cursor中的数据填充到结果队列中
	 * 
	 * @param cursor
	 */
	private List<ZcBean> fillListWithCursor(Cursor cursor) {

		List<ZcBean> resultList = new ArrayList<ZcBean>();

		ZcBean bean;
		while (cursor.moveToNext()) {

			bean = new ZcBean();
			bean.setQcsl(cursor.getInt(cursor.getColumnIndex("qcsl")));
			bean.setId(cursor.getString(cursor.getColumnIndex("id")));
			bean.setMxbh(cursor.getString(cursor.getColumnIndex("mxbh")));
			bean.setMc(cursor.getString(cursor.getColumnIndex("mc")));
			String rq = cursor.getString(cursor.getColumnIndex("rq"));
			bean.setRq(rq.substring(0, 10));
			bean.setMxsybm(cursor.getString(cursor.getColumnIndex("mxsybm")));
			bean.setGgxh(cursor.getString(cursor.getColumnIndex("ggxh")));
			bean.setDj(cursor.getInt(cursor.getColumnIndex("dj")));
			bean.setKm(cursor.getString(cursor.getColumnIndex("km")));
			bean.setMxbgr(cursor.getString(cursor.getColumnIndex("mxbgr")));
			bean.setMxje(cursor.getFloat(cursor.getColumnIndex("mxje")));
			bean.setMxjsje(cursor.getFloat(cursor.getColumnIndex("mxjsje")));
			bean.setMxjssl(cursor.getInt(cursor.getColumnIndex("mxjssl")));
			bean.setMxsl(cursor.getInt(cursor.getColumnIndex("mxsl")));
			bean.setSl(cursor.getInt(cursor.getColumnIndex("sl")));
			bean.setMxsynx(cursor.getInt(cursor.getColumnIndex("mxsynx")));
			bean.setMxxglbm(cursor.getString(cursor.getColumnIndex("mxxglbm")));
			bean.setQcrq(cursor.getString(cursor.getColumnIndex("qcrq")));
			bean.setQcbfbz(cursor.getInt(cursor.getColumnIndex("qcbfbz")));
			bean.setQctzbz(cursor.getInt(cursor.getColumnIndex("qctzbz")));
			bean.setQctmbz(cursor.getInt(cursor.getColumnIndex("qctmbz")));
			bean.setQctpbz(cursor.getInt(cursor.getColumnIndex("qctpbz")));
			bean.setRzbz(cursor.getInt(cursor.getColumnIndex("rzbz")));
			bean.setTzdh(cursor.getInt(cursor.getColumnIndex("tzdh")));
			bean.setXh(cursor.getInt(cursor.getColumnIndex("xh")));
			bean.setZy(cursor.getString(cursor.getColumnIndex("zy")));
			bean.setMxsyzk(cursor.getInt(cursor.getColumnIndex("mxsyzk")));
			resultList.add(bean);
			bean = null;
		}

		return resultList;
	}

	/**
	 * 根据部门名称获得部门编码
	 * 
	 * @param bmmc
	 *            部门名称
	 * @return 部门编码
	 */
	public String getBmbm(String bmmc) {

		String sql = "select bmbm from t_bm where bmmc =?";

		Cursor cursor = db.rawQuery(sql, new String[] { bmmc });

		if (cursor != null) {
			cursor.moveToFirst();
			return cursor.getString(cursor.getColumnIndex("bmbm"));
		}

		return null;

	}

	/**
	 * 查询资产的清查时间
	 * 
	 * @param bmmc
	 *            部门名称
	 * @return 清查时间yyyy-mm-dd HH:mm 格式
	 */
	public String getQcrq(String bmmc) {

		String qcrq = null;

		Cursor cursor = db.rawQuery("select qcrq from " + TABLE_BM
				+ " where bmmc=?;", new String[] { bmmc });

		if (cursor != null) {

			if (cursor.moveToNext()) {

				qcrq = cursor.getString(cursor.getColumnIndex("qcrq"));
			}
		}

		return qcrq;
	}

	/**
	 * 更新部门清查时间
	 * 
	 * @param bmmc
	 *            部门名称
	 * @param qcrq
	 *            清查日期
	 */
	public void updateBmQcrq(String bmmc, String qcrq) {

		ContentValues values = new ContentValues();
		values.put("qcrq", qcrq);
		db.update(TABLE_BM, values, "bmmc=?", new String[] { bmmc });

	}

	/**
	 * 根据部门名称和清查日期将符条件的资产将清查状态置为未清查 并且也需要将部门表中的清查日期置为空
	 * 
	 * @param bmmc
	 * @param qcrq
	 */
	public void clearQcDatas(String bmmc, String qcrq) {

		String[] selectionArgs = new String[2];
		selectionArgs[0] = qcrq;
		selectionArgs[1] = "%" + bmmc;

		ContentValues values = new ContentValues();
		values.put("qcsl", 0);
		db.update(TABLE_ZC, values, "qcrq=? and mxsybm like ?", selectionArgs);

		values = new ContentValues();
		values.put("qcrq", "");
		db.update(TABLE_BM, values, "bmmc=?", new String[] { bmmc });

	}

	/**
	 * 通过科目名称找到科目编号
	 * 
	 * @param kmmc
	 * @return
	 */
	public String getKmbm(String kmmc) {

		String kmbh = null;

		Cursor cursor = db.rawQuery("select kmbm from " + TABLE_KM
				+ " where kmmc=?;", new String[] { kmmc });

		if (cursor != null) {

			if (cursor.moveToNext()) {

				kmbh = cursor.getString(cursor.getColumnIndex("kmbm"));
			}
		}

		return kmbh;
	}

	/**
	 * 通过使用状况编码找到使用状况名称
	 * 
	 * @param 使用状况编码
	 * @return 使用状况名称
	 */
	public String getSyzkMc(int zcsyzkbm) {

		Cursor cursor = db.rawQuery("select zcsyzkmc from " + TABLE_SYZK
				+ " where zcsyzkbm=?;",
				new String[] { String.valueOf(zcsyzkbm) });

		if (cursor != null) {

			if (cursor.moveToFirst())
				return cursor.getString(cursor.getColumnIndex("zcsyzkmc"));

		}

		return null;
	}

	/**
	 * 根据编号查询记录,以编号结尾的mxbh
	 * 
	 * @param bh
	 * @return
	 */
	public ZcBean query(String bh) {

		ZcBean bean = null;

		String sql = "select * from " + TABLE_ZC + " where mxbh like ?;";

		Cursor cursor = db.rawQuery(sql, new String[] { "%" + bh });

		if (cursor != null) {
			if (cursor.moveToFirst()) {

				bean = new ZcBean();

				bean.setMxbh(cursor.getString(cursor.getColumnIndex("mxbh")));
				bean.setKm(cursor.getString(cursor.getColumnIndex("km")));
				bean.setMc(cursor.getString(cursor.getColumnIndex("mc")));
				bean.setGgxh(cursor.getString(cursor.getColumnIndex("ggxh")));
				bean.setMxxglbm(cursor.getString(cursor
						.getColumnIndex("mxxglbm")));
				bean.setMxsybm(cursor.getString(cursor.getColumnIndex("mxsybm")));

			}
		}
		return bean;

	}

	/**
	 * 更新资产
	 * 
	 * @param zcBean
	 */
	public void update(ZcBean zcBean) {

		ContentValues contentValues = new ContentValues();
		contentValues.put("qcrq", zcBean.getQcrq());
		contentValues.put("qcbfbz", zcBean.getQcbfbz());
		contentValues.put("qctzbz", zcBean.getQctzbz());
		contentValues.put("qctmbz", zcBean.getQctmbz());
		contentValues.put("qctpbz", zcBean.getQctpbz());
		contentValues.put("qcsl", zcBean.getQcsl());

		db.update(TABLE_ZC, contentValues, "mxbh=?",
				new String[] { zcBean.getMxbh() });

	}

	/**
	 * 根据科目名称筛选出其子科目
	 * 
	 * @param mc
	 *            科目名称
	 * @return
	 */
	public List<KmBean> getKmList(String mc) {

		List<KmBean> kmList = new ArrayList<KmBean>();
		String bm = "";
		if (!TextUtils.isEmpty(mc))
			bm = getKmbm(mc);
		String sql = "select kmbm,kmmc from " + TABLE_KM;

		Cursor cursor = db.rawQuery(sql, null);

		if (cursor != null) {

			KmBean kmBean = null;

			KmBean bean = null;
			int id = 1;

			while (cursor.moveToNext()) {

				String kmbm = cursor.getString(cursor.getColumnIndex("kmbm"));
				String kmmc = cursor.getString(cursor.getColumnIndex("kmmc"));

				// 编码不符合
				if (!kmbm.startsWith(bm)) {
					continue;
				}

				int pid = 0;

				for (int i = 0; i < kmList.size(); i++) {

					bean = kmList.get(i);

					String sub = kmbm.substring(0, kmbm.length() - 1);

					if (bean.getKmbm().equals(sub)) {

						pid = bean.getId();
						break;

					}

					bean = null;

				}

				kmBean = new KmBean(id++, pid, kmmc, kmbm);
				kmList.add(kmBean);
				kmBean = null;

			}

		}

		return kmList;

	}

	/**
	 * 根据指定部门名称筛选出其子部门
	 * 
	 * @param mc
	 * @return
	 */
	public List<BmBean> getBmList(String mc) {

		List<BmBean> bmList = new ArrayList<BmBean>();

		String bm = "";

		if (!TextUtils.isEmpty(mc))
			bm = getBmbm(mc);

		String sql = "select bmbm,bmmc from " + TABLE_BM;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor != null) {

			BmBean bmBean = null;

			BmBean bean = null;
			int id = 1;

			while (cursor.moveToNext()) {

				String bmbm = cursor.getString(cursor.getColumnIndex("bmbm"));
				String bmmc = cursor.getString(cursor.getColumnIndex("bmmc"));

				// 編碼不是父編碼的子集
				if (!bmbm.startsWith(bm)) {
					continue;
				}

				int pid = 0;

				for (int i = 0; i < bmList.size(); i++) {

					bean = bmList.get(i);
					String sub = bmbm.substring(0, bmbm.length() - 2);
					if (TextUtils.isEmpty(sub)) {
						pid = 0;
						break;
					} else {

						if (bean.getBmbm().equals(sub)) {

							pid = bean.getId();
							break;

						}

					}
					bean = null;
				}

				bmBean = new BmBean(id++, pid, bmmc, bmbm);

				bmList.add(bmBean);
				bmBean = null;

			}

		}

		return bmList;

	}
}
