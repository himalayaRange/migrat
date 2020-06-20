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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author wangyi
 */
@Configuration 
public class CorsConfig { 
	
	@Bean 
	public CorsFilter corsFilter() { 
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); 
		CorsConfiguration corsConfiguration = new CorsConfiguration(); 
		// 1 设置访问源地址 
		corsConfiguration.addAllowedOrigin("*"); 
		// 2 设置访问源请求头 
		corsConfiguration.addAllowedHeader("*"); 
		// 3 设置访问源请求方法 
		corsConfiguration.addAllowedMethod("*"); 
		// 4.允许跨域携带cookie,前后端分离支持
		corsConfiguration.setAllowCredentials(true);
		// 5 对接口配置跨域设置 
		source.registerCorsConfiguration("/**", corsConfiguration); 
		return new CorsFilter(source); 
	} 
	
}