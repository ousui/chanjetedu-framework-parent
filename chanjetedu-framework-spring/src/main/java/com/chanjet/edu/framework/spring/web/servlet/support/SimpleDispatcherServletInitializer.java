package com.chanjet.edu.framework.spring.web.servlet.support;

import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import lombok.Getter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * Created by shuai.w on 2016/6/22.
 */
public abstract class SimpleDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Getter
	private String springPropertiesFile = "spring.properties";

	@Override
	protected final Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{getSpringAppConfig()};
	}

	@Override
	protected final Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{getSpringMvcConfig()};
	}

	@Override
	protected final String[] getServletMappings() {
		return new String[]{"/"};
	}

	@Override
	public final void onStartup(ServletContext servletContext) throws ServletException {
		for (Map.Entry<String, String> kv : getInitParameter().entrySet()) {
			servletContext.setInitParameter(kv.getKey(), kv.getValue());
		}
		super.onStartup(servletContext);
	}

	private Map<String, String> getInitParameter() {
		URL url = Resources.getResource(getSpringPropertiesFile());
		Properties properties = new Properties();
		try {
			properties.load(Resources.asByteSource(url).openStream());
		} catch (IOException e) {
			logger.warn("file [" + getSpringPropertiesFile() + "] not found in classpath.");
		}
		Enumeration<String> e = (Enumeration<String>) properties.propertyNames();

		Map map = Maps.newHashMap();

		while (e.hasMoreElements()) {
			String key = e.nextElement();
			map.put(key, properties.getProperty(key));
		}
		Map extend = getExtendInitParameter();
		if (extend != null) {
			map.putAll(getExtendInitParameter());
		}
		return map;

	}

	protected Map<String, String> getExtendInitParameter() {
		return null;
	}

	protected abstract Class<?> getSpringAppConfig();

	protected abstract Class<?> getSpringMvcConfig();
}
