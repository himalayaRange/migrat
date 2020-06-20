/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.ymy.xxb.migrat.module.comyany.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.comyany.constant.ModuleConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.Jedis;

/**
 * 
 * 分布式锁四要素：
 *   
 *   1. 互斥性
 *   
 *   2. 不会发生死锁
 *   
 *   3. 具有容错性
 *   
 *   4. 加锁解锁必须由同一客户端处理
 *
 * @author: wangyi
 *
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.API.API_MODULE_CACHAE)
@Api(value = "CacheController API", tags = "cache", description = "缓存控制器")
public class CacheController {
	private static final Long RELESE_SUCCESS = 1L;
	private static final String LOCK_SUCCESS = "OK";
	private static final String SET_IF_EXIST = "NX";
	private static final String SET_WITH_EXPIRE_TIME = "EX";
	private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
	
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	
	
	/**
	 * 
	 * 该方法仅支持单实例的Redis可实现分布式锁，对于集群模式无法使用
	 * 
	 * 支持重复，线程安全
	 * 
	 * @param lockKey 加锁键
	 * @param clientId 客户端唯一标识（采用UUID）
	 * @param timeOut 超时时间
	 * @return
	 */
	@PostMapping("tryLock")
	@ApiOperation(value = "Redis NX 获取锁" , httpMethod = "POST"  ,  notes = "单实例的Redis可实现分布式锁，单位/秒，对于集群模式无法使用,支持重复，线程安全" , produces = "application/json;charset=UTF-8")
	public SoulResult tryLock(
			@RequestParam(value = "lockKey", required = true) String lockKey,
			@RequestParam(value = "clientId", required = true) String clientId,
			@RequestParam(value = "timeOut", required = true) long timeOut
			) {
		// 只能通过execute的回调获取执行的结果
		Boolean lock = redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
			Jedis jedis = (Jedis)redisConnection.getNativeConnection();
			String result = jedis.set(lockKey, clientId, SET_IF_EXIST, SET_WITH_EXPIRE_TIME, timeOut);
			if (LOCK_SUCCESS.equals(result)) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		});
		return SoulResult.success("获取锁请求成功！", lock);
	}
	
	
	/**
	 * 
	 * 释放锁
	 * 
	 * @param lockKey 加锁键
	 * @param clientId 客户端唯一标识
	 * @return
	 */
	@PostMapping("releaseLock")
	@ApiOperation(value = "Redis 释放锁" , httpMethod = "POST"  ,  notes = "单实例的Redis可实现分布式锁，对于集群模式无法使用,支持重复，线程安全" , produces = "application/json;charset=UTF-8")
	public SoulResult releaseLock(
			@RequestParam(value = "lockKey", required = true) String lockKey,
			@RequestParam(value = "clientId", required = true) String clientId) {
		Boolean release = redisTemplate.execute((RedisCallback<Boolean>) redisConnnection -> {
			Jedis jedis = (Jedis)redisConnnection.getNativeConnection();
			Object result = jedis.eval(RELEASE_LOCK_SCRIPT, Collections.singletonList(lockKey), Collections.singletonList(clientId));
			if (RELESE_SUCCESS.equals(result)) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		});
		return SoulResult.success("释放锁请求成功!", release);
	}
	
	
	@PostMapping("testLock")
	@ApiOperation(value = "获取锁测试" , httpMethod = "POST"  ,  notes = "获取锁测试" , produces = "application/json;charset=UTF-8")
	public SoulResult testLock() {
		String lockKey = "REDIS_LOCK_KEY";
		String clientId = UUID.randomUUID().toString();
		Long timeOut = 15L;
		return tryLock(lockKey, clientId, timeOut);
	}
	
}
