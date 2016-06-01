package com.chanjet.edu.framework.mybatis.scripting.velocity;


import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.mybatis.scripting.velocity.Driver;
import org.mybatis.scripting.velocity.SQLScriptSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by shuai.w on 2016/5/26.
 */
public class VelocityLanguageDriver extends Driver {
	private final static Logger LOGGER = LoggerFactory.getLogger(VelocityLanguageDriver.class);

	public static String PROP_FILE = "mybatis-velocity.properties";

	public static String BASE_PATH = "mybatis-mapper/";

	private static String basePath = BASE_PATH;

	static {
		Properties properties = new Properties();
		try {
			InputStream is = VelocityLanguageDriver.class.getClassLoader().getResourceAsStream(PROP_FILE);
			if (is != null) {
				properties.load(is);
				LOGGER.debug("加载配置{}文件", PROP_FILE);
				basePath = properties.getProperty("velocity-sql-path", BASE_PATH);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterTypeClass) {
		return sqlSource(configuration, script.getNode().getTextContent(), parameterTypeClass);
	}

	@Override
	public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterTypeClass) {
		return sqlSource(configuration, script, parameterTypeClass);
	}

	private SqlSource sqlSource(Configuration configuration, String content, Class<?> parameterTypeClass) {
		// 如果以 .vm 结尾，表明是一个文件，解析这个地址，并读出文件内容
		if (content.trim().toLowerCase().endsWith(".vm")) {
			LOGGER.debug("SQL 源为 .vm 结尾，判定为 vm 文件，加载 vm 文件中的 sql 语句。");
			// TODO 查找等多个路径
			try {
				URL url = Resources.getResource(Paths.get(basePath, content).toString().replace("\\", "/").trim());
				content = Resources.toString(url, Charsets.UTF_8);
			} catch (IOException e) {
//				e.printStackTrace();
				throw new RuntimeException(e.getLocalizedMessage());
			}
		}
		return new SQLScriptSource(configuration, content, parameterTypeClass == null ? Object.class : parameterTypeClass);
	}
}
