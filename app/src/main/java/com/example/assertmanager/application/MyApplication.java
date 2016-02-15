package com.example.assertmanager.application;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.example.assertmanager.db.DBManager;

public class MyApplication extends Application {

	public static Context wholeContext;
		
	public static int popWindowWidth;//显示框的宽度，为屏幕一半
	public static int popWindowHeight;//显示框的高度，为屏幕一半
	
	@Override
	public void onCreate() {
		super.onCreate();

		wholeContext=this;
		
		new Thread(new Runnable() {

			@Override
			public void run() {

				DBManager.getInstance();

			}
		}).start();

		
		DisplayMetrics metrics=getResources().getDisplayMetrics();
		popWindowWidth=metrics.widthPixels/2;
		popWindowHeight=metrics.heightPixels/2;
		
	}

}
