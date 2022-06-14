
package com.truist.account.poc.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.ehcache.config.CacheConfiguration;
@EnableCaching
@Configuration
public class CacheConfig extends CachingConfigurerSupport {
	Logger logger = LogManager.getLogger(CacheConfig.class);
	@Bean
	public APIFilter aPIFilter() {
		return new APIFilter();
	}
	
	@Value("${cache-in-second-value}")
	private long casheInSecond;
	
	@Value("${cache-name-for-emplist}")
	private String casheNameEmpList;
	
	@Value("${memory-store-eviction-policy}")
	private String evictPolicy;
	
	
	@Bean
	public net.sf.ehcache.CacheManager ehCacheManager() {
		CacheConfiguration tenSecondCache = new CacheConfiguration();
		tenSecondCache.setName("second-cache");
		tenSecondCache.setMemoryStoreEvictionPolicy(evictPolicy);
		tenSecondCache.setMaxEntriesLocalHeap(1000);
		tenSecondCache.setTimeToLiveSeconds(10);
		logger.info("casheInSecond--"+casheInSecond+"--casheNameEmpList"+casheNameEmpList);
		CacheConfiguration twentySecondCache = new CacheConfiguration();
		twentySecondCache.setName(casheNameEmpList);
		twentySecondCache.setMemoryStoreEvictionPolicy(evictPolicy);
		twentySecondCache.setMaxEntriesLocalHeap(1000);
		twentySecondCache.setTimeToLiveSeconds(casheInSecond);

		net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
		config.addCache(tenSecondCache);
		config.addCache(twentySecondCache);
		return net.sf.ehcache.CacheManager.newInstance(config);
	}


	@Bean
	@Override
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheManager());
	}
}