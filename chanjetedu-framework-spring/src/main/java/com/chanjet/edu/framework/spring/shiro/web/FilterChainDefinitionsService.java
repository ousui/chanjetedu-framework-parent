package com.chanjet.edu.framework.spring.shiro.web;

import java.util.Map;

/**
 * Created by shuai.w on 2016/6/8.
 */
public interface FilterChainDefinitionsService {

	/**
	 * 重新加载框架权限资源配置 (强制线程同步)
	 */
	void updateFilterChain();

	/**
	 * 初始化次要权限
	 */
	Map<String, String> getCustomFilterChain();

	/**
	 * 查詢所有已經配置的過濾器
	 * @return
	 */
	Map<String, String> getAllFilterChain();
}
