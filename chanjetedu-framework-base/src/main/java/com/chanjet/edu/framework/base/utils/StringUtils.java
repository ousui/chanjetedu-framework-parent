package com.chanjet.edu.framework.base.utils;

import java.text.NumberFormat;

/**
 * Created by shuai.w on 2015/12/16.
 */
public class StringUtils extends org.springframework.util.StringUtils {


	/**
	 * 绝对比较两个字符串是否一样，可以比较两个 NULL 值。
	 *
	 * @author 王帅 @2010-8-24
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isSameWithNull(String str1, String str2) {
		return String.valueOf(str1).equals(String.valueOf(str2));
	}

	/**
	 * 比较两个字符串的文本是否完全一样，排除空格。
	 *
	 * @author 王帅 @2010-8-24
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isTextSame(String str1, String str2) {
		return String.valueOf(str1).trim().equals(String.valueOf(str2).trim());
	}

	/**
	 * 根据固定长度补齐字符串，如果超长则返回本身。
	 *
	 * @author 王帅 @2010-8-19
	 * @param str
	 * @param fillChar
	 * @param totalLength
	 * @return
	 */
	public static String appendChar(Object val, char fillChar, int totalLength) {
		String v = val.toString();
		for (int i = (totalLength - v.length()); i > 0; i--) {
			v = v + fillChar;
		}
		return v;
	}

	public static String fillChar(Object val, char fillChar, int totalLength) {
		String v = val.toString();
		for (int i = (totalLength - v.length()); i > 0; i--) {
			v = fillChar + v;
		}
		return v;
	}

	/**
	 * 移除HTML标签
	 */
	public static String removeHtmlTag(String html) {
		return html.replaceAll("<.*?>", "").replaceAll("&", "&amp;").replaceAll("\"", "&quot;");
	}

	/**
	 * 转义 HTML 标签到页面可显示标签。
	 */
	public static String conveyHtmlToPage(String html) {
		return html.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&", "&amp;").replaceAll("\"", "&quot;");
	}

	/**
	 * 获取某个最后的字符之后的所有字符。
	 *
	 * @author 王帅 @2010-8-24
	 * @param str
	 * @param sp
	 * @param withSp
	 *            是否带 sp 本身
	 * @return
	 */
	public static String getLastFromSp(String src, String sp, boolean withSp) {
		String str = src;
		if (str.contains(sp)) {
			str = str.substring(str.lastIndexOf(sp) + (withSp ? 0 : sp.length()), str.length());
		}
		return str;
	}

	/**
	 * 获取某个字符之前的所有字符。
	 *
	 * @author 王帅 @2010-8-24
	 * @modifier zw 2011-02-28获取某个字符之前的字符，substring第二个参数的结束下标值不应该减一 str =
	 *           str.substring(0, str.lastIndexOf(sp) - 1);
	 * @param str
	 * @param sp
	 * @return
	 */
	public static String getFrontBeforeSp(String str, String sp) {
		if (str.contains(sp)) {
			// str = str.substring(0, str.lastIndexOf(sp) - 1);
			str = str.substring(0, str.lastIndexOf(sp));
		}
		return str;
	}

	public static String subStringFirst(String str, String sp) {
		if (str.length() >= sp.length()) {
			return str.substring(str.indexOf(sp) + sp.length());
		}
		return str;
	}

	/**
	 * 将max值乘于1.5并将除第一位的数字补0 如参数max值为2002 则返回值 3000 主要适用于柱状图的修改
	 *
	 * @param max
	 * @return
	 */
	public static String convertMaxValue(double max) {
		max = max * 1.5;
		String temp = String.valueOf(max);
		if (temp.contains(".")) {
			temp = temp.substring(0, temp.indexOf("."));
		}
		int m = 0;
		m = Integer.parseInt(temp.substring(0, 1));
		if (temp.length() >= 2) {
			int z = Integer.parseInt(temp.substring(1, 2));
			if (z >= 5) {
				m = m + 1;
			}
		}
		for (int i = 1; i < temp.length(); i++) {
			m = m * 10;
		}
		return String.valueOf(m);
	}

	/**
	 * int 数组转换为以某个字符分隔的字符串
	 *
	 * @author 王帅 @2010-9-18
	 * @param arr
	 * @param dou
	 * @return
	 */
	public static String intArrayToString(int[] arr, String dou) {
		String str = "";
		for (int i : arr) {
			str += i + dou;
		}
		return str.substring(0, str.lastIndexOf(dou));
	}

	public static int[] stringToInts(String vals, String split) {
		if (!hasText(vals)) {
			return null;
		}
		String[] va = vals.split(split);
		int[] tt = new int[va.length];
		for (int i = 0; i < tt.length; i++) {
			tt[i] = Integer.valueOf(va[i]);
		}
		return tt;
	}

	public static String getStringWithReplace(final String src, final String replace, Object... objects) {
		String rep, str = src;
		for (Object object : objects) {
			if (object == null) {
				rep = "";
			} else {
				rep = object.toString();
			}
			str = str.replaceFirst(replace, rep);
		}
		return str;
	}

	public static String getPercent(Number no) {
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);
		return nf.format(no);
	}

	public static String underline2camel(String str) {
		return underline2camel(str, false);
	}

	public static String underline2camel(String str, boolean capitalized) {
		if (!hasText(str)) {
			return str;
		}
		StringBuilder s = new StringBuilder();

		String[] strs = str.split("_");
		for (String ss : strs) {
			s.append(capitalize(ss));
		}
		if (capitalized) {
			return s.toString();
		} else {
			return uncapitalize(s.toString());
		}
	}

	public static String camel2underline(String str) {
		return camel2underline(str, false);
	}

	public static String camel2underline(String str, boolean capitalized) {
		StringBuilder s = new StringBuilder();

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Character.isUpperCase(c) && i > 0) {
				s.append("_");
			}
			s.append(c);
		}
		if (capitalized) {
			return capitalize(s.toString().toLowerCase());
		} else {
			return s.toString().toLowerCase();
		}
	}
}
