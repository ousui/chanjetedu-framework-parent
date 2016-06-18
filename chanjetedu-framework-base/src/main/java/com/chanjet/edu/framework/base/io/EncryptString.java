package com.chanjet.edu.framework.base.io;

import com.google.common.hash.HashCode;
import com.google.common.io.BaseEncoding;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.ParseException;

/**
 * 一个简单的字符串解密扩展，输入字符串以后，可以根据类型输出对应的解密后的字符串
 * Created by shuai.w on 2016/6/17.
 */
public class EncryptString implements Serializable {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String TPL = "{0}@{1}";

	@Getter
	private Type type;

	@Getter
	private String encodeString;

	private final byte[] bytes;

	private final String decodeString;

	public EncryptString(String source) throws ParseException {
		bytes = parse(source);
		decodeString = new String(bytes);
	}

	private byte[] parse(String source) throws ParseException {
		Object[] objects = new MessageFormat(TPL).parse(source);
		type = Type.valueOf(objects[0].toString());
		encodeString = objects[1].toString();
		byte[] bytes;
		switch (type) {
			case BASE64:
				bytes = BaseEncoding.base64().decode(encodeString);
				break;
			case HEX:
				bytes = HashCode.fromString(encodeString).asBytes();
				break;
			default:
				logger.warn("there is no matched Type for {}", type);
				bytes = new byte[0];
		}
		return bytes;
	}

	@Override
	public String toString() {
		return decodeString;
	}

	enum Type {
		BASE64, HEX;
	}

	public static void main(String[] args) throws ParseException {
		System.out.println(new EncryptString("BASE64@ZXhhbS1wYXBlci1zdG9yZQ=="));
	}
}
