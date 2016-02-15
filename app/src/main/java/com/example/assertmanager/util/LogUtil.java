package com.example.assertmanager.util;

import android.util.Log;

public class LogUtil {

	public static String TAG = "AssertManager";

	private final static int LOG_VERBOSE = 1;

	private final static int LOG_DEBUG = 2;

	private final static int LOG_INFO = 3;

	private final static int LOG_WARN = 4;

	private final static int LOG_ERROR = 5;

	private final static int NOTHING = 6;

	public final static int LEVEL = LOG_VERBOSE;

	public static void v(String msg) {

		if (LEVEL <= LOG_VERBOSE)
			Log.v(TAG, msg);

	}

	public static void d(String msg) {

		if (LEVEL <= LOG_DEBUG)
			Log.d(TAG, msg);

	}

	public static void i(String msg) {

		if (LEVEL <= LOG_INFO)
			Log.i(TAG, msg);

	}

	public static void w(String msg) {

		if (LEVEL <= LOG_WARN)
			Log.w(TAG, msg);

	}

	public static void e(String msg) {

		if (LEVEL <= LOG_ERROR)
			Log.e(TAG, msg);

	}
}
