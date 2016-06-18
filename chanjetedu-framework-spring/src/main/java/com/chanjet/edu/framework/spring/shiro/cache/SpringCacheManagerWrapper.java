package com.chanjet.edu.framework.spring.shiro.cache;

import lombok.Setter;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.Collection;
import java.util.Set;

/**
 * 针对 shiro 的 cache manager，使用 spring 进行抽象，统一使用 spring 管理。
 * Created by shuai.w on 2016/6/8.
 */
public class SpringCacheManagerWrapper implements org.apache.shiro.cache.CacheManager {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Setter
	private CacheManager cacheManager;

	@Override
	public <K, V> org.apache.shiro.cache.Cache<K, V> getCache(String name) throws CacheException {
		Cache springCache = cacheManager.getCache(name);
		logger.debug("use spring cache manager: {}", name);
		return new SpringCacheWrapper(springCache);
	}


	static class SpringCacheWrapper implements org.apache.shiro.cache.Cache {
		private final Cache springCache;

		SpringCacheWrapper(Cache springCache) {
			this.springCache = springCache;
		}

		@Override
		public Object get(Object key) throws CacheException {
			Object value = springCache.get(key);
			if (value instanceof SimpleValueWrapper) { // 针对 spring cache 特性，需要检测 simple value wrapper
				return ((SimpleValueWrapper) value).get();
			}
			return value;
		}

		@Override
		public Object put(Object key, Object value) throws CacheException {
			springCache.put(key, value);
			return value;
		}

		@Override
		public Object remove(Object key) throws CacheException {
			springCache.evict(key);
			return null;
		}

		@Override
		public void clear() throws CacheException {
			springCache.clear();
		}

		@Override
		public int size() {
			if (springCache.getNativeCache() instanceof com.google.common.cache.Cache) {
				com.google.common.cache.Cache guavaCache = (com.google.common.cache.Cache) springCache.getNativeCache();
				return Long.valueOf(guavaCache.size()).intValue();
			}
			// redis 无法直接找到 size
			throw new UnsupportedOperationException("invoke spring cache abstract size method not supported: " + springCache.getNativeCache().getClass());
		}

		@Override
		public Set keys() {
			if (springCache.getNativeCache() instanceof com.google.common.cache.Cache) {
				com.google.common.cache.Cache guavaCache = (com.google.common.cache.Cache) springCache.getNativeCache();
				return guavaCache.asMap().keySet();
			}

			throw new UnsupportedOperationException("invoke spring cache abstract size method not supported: " + springCache.getNativeCache().getClass());
		}

		@Override
		public Collection values() {
			if (springCache.getNativeCache() instanceof com.google.common.cache.Cache) {
				com.google.common.cache.Cache guavaCache = (com.google.common.cache.Cache) springCache.getNativeCache();
				return guavaCache.asMap().values();
			}

			throw new UnsupportedOperationException("invoke spring cache abstract size method not supported: " + springCache.getNativeCache().getClass());
		}
	}
}
