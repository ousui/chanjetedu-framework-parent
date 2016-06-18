package com.chanjet.edu.framework.spring.shiro.web.filter;

import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 对 roles filter 的复写，达到处理自定义逻辑的目的。
 * Created by shuai.w on 2016/6/4.
 */
public class AnyRolesAllowedFilter extends RolesAuthorizationFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String FILTER_NAME = "adv-roles";

	/**
	 * 没有权限限制的角色。
	 */
	@Setter
	private List<String> alwaysAllowRoles;

	/**
	 * 需要重写这个方法。
	 *
	 * @param request
	 * @param response
	 * @param mappedValue 是配置中传递过来的值。
	 * @return
	 * @throws IOException
	 */
	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
		Subject subject = getSubject(request, response);

		// 开启超级管理权限，判断账户是否有超级管理员权限，有的话一律放行，不必验证。
		if (ArrayUtils.contains(subject.hasRoles(alwaysAllowRoles), true)) {
			logger.debug("在列表[{}]中检测到允许的角色。", alwaysAllowRoles);
			return true;
		}

		List roles = Arrays.asList((String[])mappedValue);
		// 检测登陆结果是否包含所需角色
		return ArrayUtils.contains(subject.hasRoles(roles), true);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		Subject subject = getSubject(request, response);
		logger.debug("禁止访问操作{}", mappedValue);
		if (subject.getPrincipal() == null) {
			saveRequest(request);
			WebUtils.issueRedirect(request, response, this.getLoginUrl());
		} else {
			if (StringUtils.hasText(this.getUnauthorizedUrl())) {
				WebUtils.issueRedirect(request, response, this.getUnauthorizedUrl());
			} else {
				WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		return false;
	}

}
