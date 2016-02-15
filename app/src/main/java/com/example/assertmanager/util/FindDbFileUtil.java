package com.example.assertmanager.util;

import java.io.File;

import android.os.Environment;

public class FindDbFileUtil {

	/**
	 * 数据库文件的路径
	 */
	private static String dbPath = null;

	/**
	 * 根据数据库文件名查找数据库文件的lujing
	 * 
	 * @return 数据库文件路径
	 */
	private static void findDbFile() {

		File rootFile = Environment.getExternalStorageDirectory();

		// if(!rootFile.exists())
		// rootFile.mkdir();

		findInDir(rootFile);

	}

	/**
	 * 在文件中查找
	 * 
	 * @param dir
	 * @return
	 */
	private static void findInDir(File file) {

		if (file.isFile()) {

			if (file.getName().equals(Constant.DB_FILE_NAME)) {

				dbPath = file.getAbsolutePath();

				return;
			}

		} else if (file.isDirectory()) {

			File[] files = file.listFiles();

			if (files != null) {
				for (File f : files) {
					findInDir(f);
				}
			}

		}

	}

	public static String getDbPath() {

		if (dbPath == null)
			findDbFile();

		return dbPath;

	}
}
