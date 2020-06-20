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

package com.ymy.xxb.migrat.common.config;

import java.io.Serializable;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author wangyi
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisCacheConfig{
	
	/**
	 * org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
	 * org.springframework.data.redis.connection.RedisConnectionFactory 
	 *   
	 * 都实现了接口，可以同时支持jedis和lettuce
	 * 
	 * Springboot2.x redis序列化
	 * 
	 * Using redisson as redis operation client current
	 * 
	 * @param factory
	 * 
	 * @return
	 */
	@Bean(value = "migratRedisTemplate")
	public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory factory) {
		 RedisTemplate<String, Serializable> template = new RedisTemplate<String,Serializable>();
		 template.setConnectionFactory(factory);
		 // 替换默认序列化方式，由Java序列化转化为Jackson
		 // 此种序列化方式结果清晰、容易阅读、存储字节少、速度快，所以推荐更换
		 Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		 ObjectMapper om = new ObjectMapper();
		 om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
	     om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
	     jackson2JsonRedisSerializer.setObjectMapper(om);
	     template.setValueSerializer(jackson2JsonRedisSerializer); // redis value 序列化
	     template.setKeySerializer(new StringRedisSerializer()); // redis key 序列化
	     template.setHashKeySerializer(new StringRedisSerializer());
	     template.setHashValueSerializer(jackson2JsonRedisSerializer);
	     template.afterPropertiesSet();
	     return template;
	}
	
}
