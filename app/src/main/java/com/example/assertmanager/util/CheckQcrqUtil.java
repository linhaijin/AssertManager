package com.example.assertmanager.util;

import java.util.Date;

import com.example.assertmanager.db.DBManager;
import com.example.assertmanager.ui.CheckMainActivity;

/**
 * 检查清查日期工具类，如果CheckMainActivity中的qcrq字段为null，则需要设置qcrq字段
 * 
 * @author Xingfeng
 *
 */
public class CheckQcrqUtil {

	public static void checkQcrq() {

		if (CheckMainActivity.qcrq == null) {

			CheckMainActivity.qcrq = TimeUtil.format(new Date());
			// 更新部门表
			DBManager.getInstance().updateBmQcrq(CheckMainActivity.sybmStr,
					CheckMainActivity.qcrq);

		}

	}
}
