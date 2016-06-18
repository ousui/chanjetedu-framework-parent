package com.chanjet.edu.framework.spring.shiro.crypto;

import com.chanjet.edu.framework.base.utils.StringUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shuai.w on 2016/6/8.
 */
public class PasswordHelper {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String DEFAULT_ALGORITHM = Md5Hash.ALGORITHM_NAME;
	private static final int DEFAULT_ITERATIONS = 1000;

	private final String algorithm;
	private final int iterations;

	private PasswordHelper(String algorithm, int iterations) {
		if (!StringUtils.hasText(algorithm)) {
			logger.debug("input algorithm is empty, set default value {}", DEFAULT_ALGORITHM);
			algorithm = DEFAULT_ALGORITHM;
		}

		if (iterations < 1) {
			iterations = 1;
		}

		this.iterations = iterations;
		this.algorithm = algorithm;
	}

	public static PasswordHelper i(String algorithm, int iterations) {
		return new PasswordHelper(algorithm, iterations);
	}

	public static PasswordHelper i() {
		return new PasswordHelper(DEFAULT_ALGORITHM, DEFAULT_ITERATIONS);
	}

	public Hash buildSalt(Object salt) {
		logger.debug("build with salt {}", salt);
		return new SimpleHash(this.algorithm, salt);
	}

	public Hash encryptPassword(String password, Hash salt) {
		logger.debug("encode password with salt {}", salt);
		return new SimpleHash(this.algorithm, password, salt, iterations);
	}

	public Hash encryptPassword(String password, String salt) {
		logger.debug("encode password with salt {}", salt);
		return encryptPassword(password, buildSalt(salt));
	}
}
