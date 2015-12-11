package com.chanjet.edu.framework.spring.web.filter;

import com.alibaba.fastjson.JSON;
import org.hibernate.SessionFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Created by shuai.w on 2015/12/9.
 */
public class HibernateStatisticsFilter extends BaseFilter {

	public static final String DEFAULT_SESSION_FACTORY_BEAN_NAME = "sessionFactory";

	private String sessionFactoryBeanName = DEFAULT_SESSION_FACTORY_BEAN_NAME;

	public String getStatistics() {
		return JSON.toJSONString(lookupSessionFactory().getStatistics());
	}

	public String getOptions() {
		return JSON.toJSONString((lookupSessionFactory().getSessionFactoryOptions()));
	}

	public String getInfo() {
		return JSON.toJSONString((lookupSessionFactory().getSessionFactoryOptions()));
	}

	private SessionFactory lookupSessionFactory() {
		logger.debug("Using SessionFactory '" + getSessionFactoryBeanName() + "' for HibernateStatisticsFilter");
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		return wac.getBean(getSessionFactoryBeanName(), SessionFactory.class);
	}

	public String getSessionFactoryBeanName() {
		return sessionFactoryBeanName;
	}

	public void setSessionFactoryBeanName(String sessionFactoryBeanName) {
		this.sessionFactoryBeanName = sessionFactoryBeanName;
	}

	@Override
	protected String initWelcomeInfo() {
		return "Welcome Hibernate Statistics! You can use these commands: statistics, options";
	}
}
