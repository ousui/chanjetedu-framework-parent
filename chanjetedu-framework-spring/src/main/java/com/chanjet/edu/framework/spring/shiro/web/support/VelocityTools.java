package com.chanjet.edu.framework.spring.shiro.web.support;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.velocity.tools.config.DefaultKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * velocity 对 shiro 的支持
 * Created by shuai.w on 2016/6/20.
 */
@DefaultKey("shiro")
public class VelocityTools {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	/**
	 * 用户是否已经认证
	 *
	 * @return
	 */
	public boolean isAuthenticated() {
		return getSubject().isAuthenticated();
	}

	/**
	 * 获取用户
	 * @return
	 */
	public Object getUser() {
		return getSubject().getPrincipal();
	}

	public boolean[] hasRoles(String... roles) {
		return getSubject().hasRoles(Arrays.asList(roles));
	}

	public boolean hasAllRoles(String... roles) {
		return getSubject().hasAllRoles(Arrays.asList(roles));
	}

	public boolean hasAnyRole(String... roles) {
		return ArrayUtils.contains(hasRoles(roles), true);
	}

}
