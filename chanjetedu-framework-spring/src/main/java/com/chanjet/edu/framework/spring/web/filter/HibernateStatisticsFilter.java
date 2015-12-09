package com.chanjet.edu.framework.spring.web.filter;

import com.alibaba.fastjson.JSON;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
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
public class HibernateStatisticsFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateStatisticsFilter.class);

	public static final String DEFAULT_SESSION_FACTORY_BEAN_NAME = "sessionFactory";

	private String sessionFactoryBeanName = DEFAULT_SESSION_FACTORY_BEAN_NAME;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String uri = request.getRequestURI();
		LOGGER.debug("access uri [{}]", uri);
		if (uri.lastIndexOf("refresh/") > 0) {
			String time = uri.substring(uri.lastIndexOf("refresh/")).replaceAll("\\D", "");
			LOGGER.debug("use refresh mode, time is {}", time);
			response.setHeader("Refresh", time + "; url=" + uri);
		}

		SessionFactory sessionFactory = lookupSessionFactory();
		Statistics statistics = sessionFactory.getStatistics();

		String json = JSON.toJSONString((statistics));

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		Writer writer = response.getWriter();
		writer.write(json);
		writer.flush();
		writer.close();
	}

	private SessionFactory lookupSessionFactory() {
		LOGGER.debug("Using SessionFactory '" + getSessionFactoryBeanName() + "' for OpenSessionInViewFilter");
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		return wac.getBean(getSessionFactoryBeanName(), SessionFactory.class);
	}

	public String getSessionFactoryBeanName() {
		return sessionFactoryBeanName;
	}

	public void setSessionFactoryBeanName(String sessionFactoryBeanName) {
		this.sessionFactoryBeanName = sessionFactoryBeanName;
	}
}
