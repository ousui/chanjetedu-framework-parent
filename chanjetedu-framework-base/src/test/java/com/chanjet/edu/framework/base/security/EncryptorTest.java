package com.chanjet.edu.framework.base.security;

import org.junit.Test;
import org.springframework.core.serializer.DefaultSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by shuai.w on 2016/4/14.
 */
public class EncryptorTest {

	private File file = new File("F:/testnow.txt");
	private Encryptor encryptor = Encryptor.i("123");

	@Test
	public void encrypt() throws Exception {

		String hw = "你好啊。。。111111111111";

		FileOutputStream fos = new FileOutputStream(file);
		new DefaultSerializer().serialize(encryptor.encrypt(hw), fos);

//		byte[] s = Encryptor.i("123").encrypt("你好".getBytes());
//		System.out.println(new String(s));
//		byte[] r = Encryptor.i("123").decrypt(s);
//		System.out.println("new String(r) = " + new String(r));
//		r = Encryptor.i("123").decrypt(s);
//		System.out.println("new String(r) = " + new String(r));

	}

	@Test
	public void decrypt() throws IOException {
		FileInputStream fis = new FileInputStream(file);
		String ii = encryptor.decrypt(String.class, fis);
		System.out.println(ii);
	}


}