package com.chanjet.edu.framework.spring.web.filter;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by shuai.w on 2015/12/9.
 */
public abstract class BaseFilter extends OncePerRequestFilter {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String uri = request.getRequestURI();
		logger.debug("access uri [{}]", uri);


		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		// 添加页面刷新参数。
		if (uri.lastIndexOf("refresh/") > 0) {
			String time = uri.substring(uri.lastIndexOf("refresh/")).replaceAll("\\D", "");
			logger.debug("use refresh mode, time is {}", time);
			String refreshUrl = uri;
			String queryStr = request.getQueryString();

			if (!Strings.isNullOrEmpty(queryStr)) {
				refreshUrl += "?" + queryStr;
			}
			response.setHeader("Refresh", time + "; url=" + refreshUrl);
		}
		Writer writer = response.getWriter();
		String cmd = request.getParameter("cmd");
		String method = request.getMethod();

		String str = initWelcomeInfo();
		if (!Strings.isNullOrEmpty(cmd)) {
			try {
				str = (String) this.getClass().getMethod(method.toLowerCase() + StringUtils.capitalize(cmd)).invoke(this);
			} catch (Exception e) {
				logger.warn("exception: {}", e.getLocalizedMessage());
			}
		}
		writer.write(str);
		writer.flush();
		writer.close();
	}

	protected abstract String initWelcomeInfo();
}
