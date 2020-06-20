package com.ymy.xxb.migrat.common.service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
/**
 * @author wangyi
 */
@Component
public class RedisService {
	
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	
	/**
	 * 写入缓存
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String key, Serializable value) {
		boolean result = false;
		try {
			redisTemplate.opsForValue().set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 写入缓存并设置失效时间
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public boolean setEx(String key, Serializable value, Long expireTime) {
		boolean result = false;
		try {
			redisTemplate.opsForValue().set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 判断key是否存在
	 * @param key
	 * @return
	 */
	public boolean exist(String key) {
		return redisTemplate.hasKey(key); 
	}
	
	/**
	 * 读取key值
	 * @param key
	 * @return
	 */
	public Serializable get(String key) {
		Serializable serializable = redisTemplate.opsForValue().get(key);
		return serializable;
	}
	
	/**
	 * 删除对应的key
	 * @param key
	 * @return
	 */
	public boolean remove(String key) {
		if (exist(key)) {
			return redisTemplate.delete(key);
		}
		return false;
	}
	
	// TODO Add Other Method
}
