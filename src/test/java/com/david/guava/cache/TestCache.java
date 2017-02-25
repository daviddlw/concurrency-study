package com.david.guava.cache;

import java.sql.Time;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class TestCache {

	private static final String NIHAO = "nihao";
	private static final String DAVIDDAI = "daviddai";
	private static final Logger LOGGER = Logger.getLogger(TestCache.class);
	private LoadingCache<String, String> localCache;
	private Cache<String, String> callableCache;

	@Before
	public void setUp() {
		initCommonCache();
		initCallableCache();
	}

	/**
	 * 常规的缓存刷新方案，如果添加了refreshAfterWrite是属于正常失效以后就会刷新
	 */
	private void initCommonCache() {
		localCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(2, TimeUnit.SECONDS)
				.refreshAfterWrite(1, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
					@Override
					public String load(String arg0) throws Exception {
						return "hello, " + arg0 + "_" + System.currentTimeMillis();
					}

				});

		localCache.put(DAVIDDAI, "戴维");
		localCache.put(NIHAO, "你好");
	}

	/**
	 * 异步缓存callable刷新方案，callable只有在缓存没有效果的情况下才会被回调
	 */
	private void initCallableCache() {
		callableCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(2, TimeUnit.SECONDS)
				.removalListener(new RemovalListener<String, String>() {

					@Override
					public void onRemoval(RemovalNotification<String, String> kv) {
						System.out.println(String.format("remove event happened, key: %s, value: %s", kv.getKey(), kv.getValue()));

					}
				}).build();
		callableCache.put(DAVIDDAI, "戴维");
		callableCache.put(NIHAO, "你好");
	}

	@Test
	public void getCallableCache() {
		String result = callableCache.getIfPresent(DAVIDDAI);
		System.out.println(result);
		try {
			TimeUnit.SECONDS.sleep(3);
			System.out.println(callableCache.getIfPresent(DAVIDDAI));
			String result2 = callableCache.get(DAVIDDAI, new Callable<String>() {

				@Override
				public String call() throws Exception {
					System.out.println("缓存失效，重新调用callable加载缓存值");
					long timestamp = System.currentTimeMillis();
					callableCache.put(DAVIDDAI, "newResult_" + timestamp);
					return callableCache.getIfPresent(DAVIDDAI);
				}
			});
			System.out.println(result2);
			TimeUnit.SECONDS.sleep(3);
			String result3 = callableCache.getIfPresent(DAVIDDAI);
			System.out.println(result3);
		} catch (ExecutionException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Test
	public void testCache() {
		try {
			System.out.println(localCache.get(DAVIDDAI));
			TimeUnit.SECONDS.sleep(2);
			System.out.println(localCache.get(DAVIDDAI));
			TimeUnit.SECONDS.sleep(2);
			System.out.println(localCache.get(DAVIDDAI));
		} catch (ExecutionException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
