package com.chanjet.edu.framework.base.security;

import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;

import java.io.IOException;

/**
 * Created by shuai.w on 2016/4/18.
 */
public class ObjectEncryptor extends Encryptor {
	public static ObjectEncryptor i(String password) {
		return new ObjectEncryptor(password, EncryptType.AES);
	}

	public static ObjectEncryptor i(String password, EncryptType encryptType) {
		return new ObjectEncryptor(password, encryptType);
	}

	public ObjectEncryptor(String password, EncryptType encryptType) {
		super(password, encryptType);
	}

	@Override
	public byte[] encrypt(Object object) throws IOException {
		return new SerializingConverter().convert(super.encrypt(object));
	}

	@Override
	public <T> T decrypt(Class<T> clazz, byte[] content) throws IOException {
		content = (byte[]) new DeserializingConverter().convert(content);
		return super.decrypt(clazz, content);
	}
}
