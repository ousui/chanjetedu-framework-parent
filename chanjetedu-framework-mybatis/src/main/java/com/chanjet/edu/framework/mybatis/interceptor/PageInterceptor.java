package com.chanjet.edu.framework.mybatis.interceptor;
/**
 *
 */

import com.chanjet.edu.framework.base.pojo.Page;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author OUSUI
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageInterceptor implements Interceptor {

	@SuppressWarnings("rawtypes")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

		BoundSql sql = statementHandler.getBoundSql();

		Object paramObj = sql.getParameterObject();

		Page page = null;
		if (paramObj instanceof Page) { // 只有一个参数的时候。
			page = (Page) paramObj;
		} else if (paramObj instanceof ParamMap) { // 参数为 paramMap 的时候。
			ParamMap paramMap = (ParamMap) paramObj;
			for (Object obj : paramMap.values()) { // 循环找出第一个 page 参数。
				if (obj instanceof Page) {
					page = (Page) obj;
					break;
				}
			}
		} else { // 不能 match 的时候直接返回。
			return invocation.proceed();
		}

		if (page == null) { // page 没结果的时候直接返回。
			return invocation.proceed();
		}

		// 符合分页条件，进行分页操作查询等。
		Connection connection = (Connection) invocation.getArgs()[0];

		MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");

		executePageCount(connection, mappedStatement, sql, page); // 将查询结果放入

		String pageSql = this.buildPageSql(sql, page);
		metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);

		Object obj = invocation.proceed();
		return obj;
	}

	@SuppressWarnings("rawtypes")
	private void executePageCount(Connection connection, MappedStatement mappedStatement, BoundSql boundSql, Page page) {
		// 记录总记录数
		String countSql = buildCountSql(boundSql);

		PreparedStatement countStmt = null;
		ResultSet rs = null;
		try {
			countStmt = connection.prepareStatement(countSql);
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());

			ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), countBS);
			parameterHandler.setParameters(countStmt);

			rs = countStmt.executeQuery();
			rs.next();
			int totalCount = rs.getInt(1);
			page.setTotal(totalCount);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				countStmt.close();
			} catch (SQLException e) {
			}
		}
	}

	private String buildPageSql(BoundSql sql, Page<?> page) {
		int start = page.getStart();
		int limit = page.getSize();
		StringBuilder pageSql = new StringBuilder(sql.getSql().trim());
		pageSql.append(page.getOrders());
		pageSql.append(" LIMIT ").append(start).append(", ").append(limit);
		return pageSql.toString();
	}

	private String buildCountSql(BoundSql sql) {
		StringBuilder countSql = new StringBuilder(sql.getSql().trim());
		countSql.insert(0, "SELECT COUNT(0) FROM (");
		countSql.append(") AS _PAGE_TBL");
		return countSql.toString();
	}

	@Override
	public Object plugin(Object target) {

		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}

	}

	@Override
	public void setProperties(Properties properties) {

	}
}
