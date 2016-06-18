package com.chanjet.edu.framework.spring.shiro.web.filter;

import com.google.common.collect.Maps;
import lombok.Setter;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;

/**
 * Created by shuai.w on 2016/6/16.
 */
public class LastLoginAuthenticationFilter extends FormAuthenticationFilter {

	public static final String FILTER_NAME = "lastlogin-authc";

	public static final String DEFAULT_ATTRIBUTE_KEY_EXCEPTION_MESSAGE = "_msg";
	public static final String DEFAULT_ATTRIBUTE_KEY_LAST_LOGIN = "_lastlogin";

	@Setter
	private String lastLoginAttributeName = DEFAULT_ATTRIBUTE_KEY_LAST_LOGIN;

	@Setter
	private String exceptionMessageAttributeName = DEFAULT_ATTRIBUTE_KEY_EXCEPTION_MESSAGE;

	private Map<Class<? extends AuthenticationException>, String> exceptions = Maps.newHashMap();

	{
		exceptions.put(IncorrectCredentialsException.class, "密码错误！");
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		request.setAttribute(lastLoginAttributeName, token.getPrincipal());
		request.setAttribute(exceptionMessageAttributeName, getLocalizedMessage(e));
		return super.onLoginFailure(token, e, request, response);
	}

	/**
	 * 翻译异常及错误信息。
	 *
	 * @return
	 */
	private String getLocalizedMessage(AuthenticationException exception) {
		String msg = exceptions.get(exception.getClass());
		return msg != null ? msg : exception.getLocalizedMessage();
	}

	public LastLoginAuthenticationFilter addExceptionMapping(Class<? extends AuthenticationException> clazz, String msg) {
		exceptions.put(clazz, msg);
		return this;
	}


}
