package com.chanjet.edu.framework.spring.web.servlet.view.velocity;

import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.config.ValidScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Created by shuai.w on 2016/6/20.
 */
@DefaultKey("app")
@ValidScope(Scope.APPLICATION)
public class AppTools {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 产生随机数
	 *
	 * @return
	 */
	public int randomInt() {
		return new Random().nextInt();
	}

	/**
	 * 产生随机数
	 *
	 * @param bound 带限制
	 * @return
	 */
	public int randomInt(int bound) {
		return new Random().nextInt(bound);
	}

	public void sout(Object obj) {
		System.out.println(obj);
	}

	public void serr(Object obj) {
		System.err.println(obj);
	}

	public void debug(String format, Object... args) {
		logger.debug(format, args);
	}

	public void info(String format, Object... args) {
		logger.info(format, args);
	}

	public void error(String format, Object... args) {
		logger.error(format, args);
	}

	public void warn(String format, Object... args) {
		logger.warn(format, args);
	}

	public void trace(String format, Object... args) {
		logger.trace(format, args);
	}

}
