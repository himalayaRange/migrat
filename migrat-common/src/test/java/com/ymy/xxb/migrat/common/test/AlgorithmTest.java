package com.ymy.xxb.migrat.common.test;

import org.junit.Test;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import com.ymy.xxb.migrat.common.algorithm.fifo.FIFOCache;
import com.ymy.xxb.migrat.common.algorithm.lfu.LFUCache;
import com.ymy.xxb.migrat.common.algorithm.lru.LRUCache;

@Slf4j
public class AlgorithmTest {
	private static LRUCache<String, Integer> cache = new LRUCache<String, Integer>(10);

	@Test
	public void lruTest() {
		for (int i = 0; i < 10; i++) {
			cache.put("k" + i, i);
		}

		for (int i = 0; i < 10; i++) {
			String key = "k" + RandomUtils.nextInt(0, 9);
			cache.get(key);
		}

		log.info("cache: {}", cache);
		cache.put("k" + 10, 10);

		log.info("After running the LRU algorithm cache :'{}'", cache);

		cache.entrySet().forEach(item -> System.out.println(item.getValue()));
	}

	@Test
	public void lfuTest() {
		LFUCache<Integer, Integer> cache = new LFUCache<Integer, Integer>(2);
		cache.put(1, 1);
		cache.put(2, 2);

		System.out.println(cache.get(2));
		System.out.println(cache.get(1));
		System.out.println(cache.get(2));

		cache.put(3, 3);
		cache.put(4, 4);

		cache.cache.entrySet().forEach(item -> System.out.println(item.getKey() + "--" + item.getValue()));
	}
	
	@Test
	public void fifoTest() {
		FIFOCache<String,Integer> cache = new FIFOCache<String, Integer>(3);
		cache.put("A", 100);
		cache.put("B", 200);
		cache.put("C", 300);
		cache.put("D", 400);
		
		cache.cache.entrySet().forEach(item -> System.out.println(item.getKey() + "--" + item.getValue()));
	}
	
}
