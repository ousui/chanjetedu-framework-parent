package com.chanjet.edu.framework.base.utils;

import org.junit.Test;

/**
 * gzip 测试用例
 * Created by shuai.w on 2016/4/14.
 */
public class GZipUtilsTest {

	@Test
	public void compress() throws Exception {
		GZipUtils.compress("F:\\1.iso");
	}

	@Test
	public void decompress() throws Exception {
		GZipUtils.decompress("F:\\1.iso");
	}

}