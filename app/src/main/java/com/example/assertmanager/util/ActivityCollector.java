package com.example.assertmanager.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityCollector {

	private static List<Activity> activities = new ArrayList<Activity>();

	/**
	 * 添加Activity
	 * 
	 * @param activity
	 */
	public static void addActivity(Activity activity) {
		if (!activities.contains(activity))
			activities.add(activity);
	}

	/**
	 * 移除Activity
	 * 
	 * @param activity
	 */
	public static void removeActivity(Activity activity) {

		if (activities.contains(activity))
			activities.remove(activity);
	}

	/**
	 * 获取当前Activity
	 * 
	 * @return 返回当前Activity，如果没有则返回null
	 */
	public static Activity getCurrentActivity() {

		if (activities.size() > 0)
			return activities.get(activities.size() - 1);

		return null;
	}

	/**
	 * 退出应用
	 */
	public static void exitApp() {

		for (Activity activity : activities) {
			if (!activity.isFinishing())
				activity.finish();
		}

	}

}
