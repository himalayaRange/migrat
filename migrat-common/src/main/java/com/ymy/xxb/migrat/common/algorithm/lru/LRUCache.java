package com.ymy.xxb.migrat.common.algorithm.lru;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * LinkedHashMap实现LRU算法缓存 底层基于hash表数据结构，基于访问时间
 * <p>
 * 
 * @author wangyi
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = 3129248529539756069L;

	/**
	 * 缓存大小
	 */
	private int cacheSize;

	public LRUCache(int cacheSize) {
		super(16, (float) 0.75, true);
		this.cacheSize = cacheSize;
	}

	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {

		return size() > cacheSize;
	}

}
