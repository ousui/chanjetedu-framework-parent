package com.chanjet.edu.framework.spring.shiro.authc;

import lombok.Setter;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

/**
 * Created by shuai.w on 2016/6/4.
 */
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {

	private final Cache cache;

	@Setter
	private boolean enabledCache = false;

	/**
	 * 重试次数
	 */
	@Setter
	private int retryCount = 5;

	public RetryLimitCredentialsMatcher(CacheManager cacheManager) {
		cache = cacheManager.getCache(RetryLimitCredentialsMatcher.class.getName());
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		// 使用缓存存储次数
		String username = token.getPrincipal().toString();

		if (enabledCache) {
			Integer count = (Integer) cache.get(username);
			if (null == count) {
				count = 0;
			}
			cache.put(username, count++);

			if (count > retryCount) {
				throw new ExcessiveAttemptsException();
			}
		}

		boolean matched = super.doCredentialsMatch(token, info);
		if (matched && enabledCache) {
			cache.remove(username);
		}
		return matched;
	}

}
