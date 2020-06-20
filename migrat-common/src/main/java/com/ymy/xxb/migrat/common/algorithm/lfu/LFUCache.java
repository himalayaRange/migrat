package com.ymy.xxb.migrat.common.algorithm.lfu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 淘汰一定时间内访问次数最少的元素，如果访问次数相同，则比较最新的一次访问时间
 * 
 * @author wangyi
 *
 */
public class LFUCache<K, V> {
	private final int 				capcity ;              // 初始化容量
	
	public Map<K, V> 				cache = new HashMap<>();  // 缓存的map
	
	public Map<K, HitRate> 		count = new HashMap<>();  // 访问次数map
	
	public LFUCache(int capcity) {
		this.capcity = capcity;
	}
	
	public void put(K key, V value) {
		V v = cache.get(key);
		if (v == null) {
			// 是否达到容量
			if (cache.size() > capcity) {
				// 根据LFU算法移除最近最少使用的元素
				remove(key);
			}
			// 记录元素访问次数
			count.put(key, new HitRate(key, 1, System.nanoTime()));
		} else {
			addHitRate(key);
		}
		
		cache.put(key, value);
	}
	
	public V get(K key) {
		V v = cache.get(key);
		if (v != null) {
			addHitRate(key);
			return v;
		}
		return null;
	}
	
	public void remove(K key) {
		HitRate hr = Collections.min(count.values());
		cache.remove(hr.key);
		count.remove(hr.key);
	}
	
	// 更新访问元素状态
	public void addHitRate(K key) {
		HitRate hitRate = count.get(key);
		hitRate.hitCount = hitRate.hitCount + 1;
		hitRate.lastTime = System.nanoTime();
	}
	
	class HitRate implements Comparable<HitRate>{
		private K              		key;  	   // 缓存键
		
		private int 				hitCount;  // 访问次数统计
		
		private long               lastTime;  // 最后一次访问时间
		
		private HitRate(K key, int hitCount, long lastTime) {
			this.key = key;
			this.hitCount = hitCount;
			this.lastTime = lastTime;
		}
		
		@Override
		public int compareTo(HitRate o) {
			int compare = Integer.compare(this.hitCount,  o.hitCount);
			return compare == 0 ? Long.compare(this.lastTime, o.lastTime) : compare;
		}
	}
	
}
