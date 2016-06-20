package com.chanjet.edu.framework.spring.web.servlet.view.velocity;

import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.config.ValidScope;

import java.util.Random;

/**
 * Created by shuai.w on 2016/6/20.
 */
@DefaultKey("app")
@ValidScope(Scope.APPLICATION)
public class AppTools {

	/**
	 * 产生随机数
	 * @return
	 */
	public int randomInt() {
		return new Random().nextInt();
	}

	/**
	 * 产生随机数
	 * @param bound 带限制
	 * @return
	 */
	public int randomInt(int bound) {
		return new Random().nextInt(bound);
	}
}
