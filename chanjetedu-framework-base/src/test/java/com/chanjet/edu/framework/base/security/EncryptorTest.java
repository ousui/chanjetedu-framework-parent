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

	private File file = new File("F:/x1.dat");
	private File file2= new File("F:/x2.dat");

	@Test
	public void encrypt() throws Exception {
		Map<String, Object> hw = Maps.newHashMap();
		hw.put("K", "我知道");
		hw.put("K2", StringUtils.class);
		hw.put("K3", new BaseServiceImpl());

		FileOutputStream fos = new FileOutputStream(file);
		new DefaultSerializer().serialize(Encryptor.i("1111111111").encrypt(hw), fos);

		FileOutputStream fos2 = new FileOutputStream(file2);
		new DefaultSerializer().serialize(Encryptor.i("1111111111").encrypt(hw), fos2);
	}

	@Test
	public void decrypt() throws IOException {
		FileInputStream fis = new FileInputStream(file);
//		byte[] oo = (byte[]) new DefaultDeserializer().deserialize(fis);
//		oo = (byte[]) new DeserializingConverter().convert(oo);
//		System.out.println(oo);
//		byte[] dd = encryptor.decrypt(oo);
		HashMap ii = Encryptor.i("1111111111").decrypt(HashMap.class, fis);
		System.out.println("ii = " + ii);
	}


}