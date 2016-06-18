package com.chanjet.edu.framework.spring.shiro.web;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by shuai.w on 2016/6/8.
 */
public abstract class AbstractFilterChainDefinitionsService implements FilterChainDefinitionsService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String FILTER_TPL = "{0}[{1}]";

	@Getter
	@Setter
	private ShiroFilterFactoryBean shiroFilterFactoryBean;

	@Setter
	@Getter
	private Map<String, String> defaultfilterChain;

	/**
	 * 所有的过滤器，将来这个过滤器列表会存储到缓存当中。
	 */
	@Getter
	private Map<String, String> allFilterChain;


	@PostConstruct // 实例化的时候即加载方法
	public void intiFilterChainMap() {
		this.logger.debug("初始化权限。");
		this.updateFilterChain();
	}

	@Override
	public void updateFilterChain() {
		synchronized (shiroFilterFactoryBean) {
			AbstractShiroFilter shiroFilter;
			try {
				shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
				e.printStackTrace();
				return;
			}
			PathMatchingFilterChainResolver resolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
			DefaultFilterChainManager manager = (DefaultFilterChainManager) resolver.getFilterChainManager();

			// 清空初始配置
			manager.getFilterChains().clear();

			// 重新构建
			Map<String, String> filterChain = this.loadFilterChain();

			for (Map.Entry<String, String> entry : filterChain.entrySet()) {
				String url = entry.getKey();
				String chainDefinition = entry.getValue().trim().replace(" ", "");
				manager.createChain(url, chainDefinition);
			}

			logger.debug("reload Filter Chain.");
		}
	}


	/**
	 * 获取主要权限，将来会使用缓存存储。
	 */
	protected Map<String, String> loadFilterChain() {
		if (null == allFilterChain) {
			allFilterChain = Maps.newLinkedHashMap();
		} else {
			allFilterChain.clear();
		}
		if (getDefaultfilterChain() != null) {
			allFilterChain.putAll(getDefaultfilterChain());
		}
		Map cfmap = getCustomFilterChain();
		if (cfmap != null) {
			allFilterChain.putAll(cfmap);
		}
		return allFilterChain;
	}


	@Override
	public abstract Map<String, String> getCustomFilterChain();


}
