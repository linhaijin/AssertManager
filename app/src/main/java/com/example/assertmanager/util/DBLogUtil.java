package com.example.assertmanager.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.example.assertmanager.model.ChangeZcBean;

/**
 * 记录数据库操作的日志，保存到log目录下
 * 
 * @author Xingfeng
 *
 */
public class DBLogUtil {

	private static DBLogUtil log;

	private String logPath = Constant.LOG_PATH + "log.txt";

	private DBLogUtil() {
	}

	public static DBLogUtil getInstance() {

		if (log == null)
			log = new DBLogUtil();

		return log;

	}

	private String lineSeparator = System.getProperty("line.separator", "\n");

	public synchronized void write(ChangeZcBean bean) {

		File file = new File(logPath);
		BufferedWriter bw = null;
		BufferedOutputStream bos = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(bean.toString());
			bw.write("\r\n");
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
