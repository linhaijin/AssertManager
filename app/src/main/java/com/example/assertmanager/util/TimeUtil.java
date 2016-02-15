package com.example.assertmanager.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 将时间格式化的工具类
 * 
 * @author Xingfeng
 *
 */
public class TimeUtil {

	/**
	 * 将毫秒数转化成yyyy-MM-dd HH：mm格式
	 * 
	 * @param millseconds
	 * @return
	 */
	public static String format(long millseconds) {

		return format(new Date(millseconds));

	}

	@SuppressLint("SimpleDateFormat")
	public static String format(Date now) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(now);

	}

}
