package com.chanjet.edu.framework.base.security;

import com.chanjet.edu.framework.base.BaseServiceImpl;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.springframework.core.serializer.DefaultSerializer;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shuai.w on 2016/4/14.
 */
public class EncryptorTest {

	private File file = new File("F:/X1");
	private Encryptor encryptor = Encryptor.i("1111111111");

	@Test
	public void encrypt() throws Exception {

		Map<String, Object> hw = Maps.newHashMap();
		hw.put("K", "SSSSSSSSSSSSSSS");
		hw.put("K2", StringUtils.class);
		hw.put("K3", new BaseServiceImpl());

		FileOutputStream fos = new FileOutputStream(file);
		new DefaultSerializer().serialize(encryptor.encrypt(hw), fos);
	}

	@Test
	public void decrypt() throws IOException {
		FileInputStream fis = new FileInputStream(file);
		HashMap ii = encryptor.decrypt(HashMap.class, fis);
		System.out.println(ii);
	}


}