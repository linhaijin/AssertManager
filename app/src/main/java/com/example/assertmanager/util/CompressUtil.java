package com.example.assertmanager.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件工具类
 * 
 * @author Xingfeng
 *
 */
public class CompressUtil {

	/**
	 * 将ChangePicture文件夹与log文件夹压缩至data.zip
	 */
	public static boolean compress() {

		String outputFileName = Constant.OUTPUT_PATH + "data.zip";

		String changePicture = Constant.CHANGE_PICTURE_PATH;
		String log = Constant.LOG_PATH + "log.txt";

		File outputFile = new File(outputFileName);

		if (outputFile.exists())
			outputFile.delete();

		try {
			FileOutputStream fos = new FileOutputStream(outputFile);

			CheckedOutputStream csum = new CheckedOutputStream(fos,
					new Adler32());
			ZipOutputStream zos = new ZipOutputStream(csum);
			BufferedOutputStream out = new BufferedOutputStream(zos);

			// 写入图片
			File[] pictureFiles = new File(changePicture).listFiles();
			for (File picture : pictureFiles) {

				InputStream in = new FileInputStream(picture);
				zos.putNextEntry(new ZipEntry(picture.getPath()));
				int c;
				while ((c = in.read()) != -1)
					out.write(c);

				in.close();
				out.flush();

			}
			// 压缩日志文件
			BufferedReader in2 = new BufferedReader(new FileReader(log));
			zos.putNextEntry(new ZipEntry(log));
			int c;
			while ((c = in2.read()) != -1) {
				out.write(c);
			}

			in2.close();
			out.close();

			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
