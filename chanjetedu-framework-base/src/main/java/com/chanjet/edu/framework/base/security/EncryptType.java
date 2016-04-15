package com.chanjet.edu.framework.base.security;

import lombok.Getter;

/**
 * Created by shuai.w on 2016/4/14.
 */
public enum EncryptType {
	AES("AES"),
	DES("DES");

	@Getter
	private String name;

	EncryptType(String name) {
		this.name = name;
	}
}
