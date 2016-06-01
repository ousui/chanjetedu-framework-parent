package com.chanjet.edu.framework.mybatis.interceptor;

import com.chanjet.edu.framework.base.utils.StringUtils;
import com.chanjet.edu.framework.mybatis.interceptor.annotations.SqlTemplate;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * 需配合 mybatis-velocity 一起使用，会自动读取模板文件。
 * Created by shuai.w on 2016/5/26.
 */
@Intercepts({
		@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
		@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class VelocityLoaderInterceptor implements Interceptor {

	private final static String TPL_PREFIX = "vm";

	public VelocityLoaderInterceptor() {
		this.x = 2;
	}

	private final int x;

	public VelocityLoaderInterceptor(int x) {
		this.x = x;
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Class clazz = invocation.getClass();
		Method method = invocation.getMethod();

		System.out.println(clazz);
		System.out.println(method);

		MappedStatement ms = (MappedStatement) invocation.getArgs()[0];

		String cls = Files.getNameWithoutExtension(ms.getId());
		String mtd = Files.getFileExtension(ms.getId());
		Class clz = Class.forName(cls);
		Method md = clz.getMethod(mtd);

//		clz.getDeclaredMethod()

		Object ms1 = invocation.getArgs()[1];
		RowBounds  ms2 = (RowBounds) invocation.getArgs()[2];
		ResultHandler ms3 = (ResultHandler) invocation.getArgs()[3];

		SqlTemplate sqlTemplate = method.getAnnotation(SqlTemplate.class);
		if (sqlTemplate == null) {// 注解为空，不做处理
			return invocation.proceed();
		}

		String path = sqlTemplate.path();

		if (!StringUtils.hasText(path)) {
			String pkg = clazz.getPackage().getName();
			path = pkg.replace(".", "/") + "/" + clazz.getSimpleName() + "." + method.getName() + "." + TPL_PREFIX;
		}
		InputStream is = clazz.getResourceAsStream(path);
//		new InputStreamResource().
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8.name()));
		String sqls;
		StringBuilder sb = new StringBuilder();
		while ((sqls = reader.readLine()) != null) {
			sb.append(sqls);
		}
		sqls = sb.toString();

		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
		metaStatementHandler.setValue("delegate.boundSql.sql", sqls);
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}

	}

	@Override
	public void setProperties(Properties properties) {

	}
}
