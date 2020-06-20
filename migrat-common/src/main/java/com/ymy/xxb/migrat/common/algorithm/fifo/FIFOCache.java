package com.ymy.xxb.migrat.common.algorithm.fifo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 先进先出缓存算法
 * 
 * @author wangyi
 *
 */
public class FIFOCache<K, V> {
	private int 				maxcap;    					  // 最大容量
	public Map<K, V> 			cache;      				  // 数据缓存
	public LinkedList<K>  list = new LinkedList<>();         //  记录key添加顺序
	
	public FIFOCache(int maxcap) {
		this.maxcap = maxcap;
		cache = new HashMap<K, V>(maxcap);
	}
	
	public void put(K k, V v) {
		list.addLast(k); 									  // 记录key顺序
		if (list.size() > maxcap) {
			K first = list.getFirst();
			list.removeFirst(); 							  // 移除第一个元素
			cache.remove(first);
		}
		cache.put(k, v);
	}
	
}
