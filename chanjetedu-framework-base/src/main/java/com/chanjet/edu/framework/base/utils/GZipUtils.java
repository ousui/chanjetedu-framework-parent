package com.chanjet.edu.framework.base.utils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP 压缩工具包
 * Created by shuai.w on 2016/4/14.
 */
public abstract class GZipUtils {
	public static final int BUFFER = 1024;

	/**
	 * 原始的流压缩
	 *
	 * @param is
	 * @param os
	 * @throws IOException
	 */
	public static void compress(InputStream is, OutputStream os) throws IOException {
		GZIPOutputStream gos = new GZIPOutputStream(os);

		int count;
		byte data[] = new byte[BUFFER];
		while ((count = is.read(data, 0, BUFFER)) != -1) {
			gos.write(data, 0, count);
		}
		gos.finish();
		gos.flush();
		gos.close();
	}

	/**
	 * 数据压缩
	 *
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public static byte[] compress(byte[] data) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 压缩
		compress(bais, baos);

		byte[] output = baos.toByteArray();

		baos.flush();
		baos.close();

		bais.close();

		return output;
	}

	/**
	 * 文件压缩
	 *
	 * @param file
	 * @throws Exception
	 */
	public static boolean compress(File file) {
		try {
			File copy = new File(file.getAbsolutePath()+"~copy");
			file.renameTo(copy);
			FileInputStream fis = new FileInputStream(copy);
			FileOutputStream fos = new FileOutputStream(file);
			compress(fis, fos);
			fis.close();
			fos.flush();
			fos.close();
			copy.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return file.exists();
	}

	/**
	 * 文件压缩
	 *
	 * @param path
	 * @throws Exception
	 */
	public static boolean compress(String path) throws Exception {
		return compress(new File(path));
	}

	/**
	 * 数据解压缩
	 *
	 * @param is
	 * @param os
	 * @throws IOException
	 * @throws Exception
	 */
	public static void decompress(InputStream is, OutputStream os) throws IOException {
		GZIPInputStream gis = new GZIPInputStream(is);

		int count;
		byte data[] = new byte[BUFFER];
		while ((count = gis.read(data, 0, BUFFER)) != -1) {
			os.write(data, 0, count);
		}

		gis.close();
	}

	/**
	 * 数据解压缩
	 *
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static byte[] decompress(byte[] data) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// 解压缩

		decompress(bais, baos);

		data = baos.toByteArray();

		baos.flush();
		baos.close();

		bais.close();

		return data;
	}


	/**
	 * 文件解压缩
	 *
	 * @param file
	 * @throws Exception
	 */
	public static boolean decompress(File file) {
		try {
			File copy = new File(file.getAbsolutePath()+"~copy");
			file.renameTo(copy);
			FileInputStream fis = new FileInputStream(copy);
			FileOutputStream fos = new FileOutputStream(file);

			decompress(fis, fos);
			fis.close();
			fos.flush();
			fos.close();

			copy.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return file.exists();

	}


	/**
	 * 文件解压缩
	 *
	 * @param path
	 * @throws Exception
	 */
	public static boolean decompress(String path) {
		return decompress(new File(path));
	}


}
