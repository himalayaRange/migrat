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
package com.ymy.xxb.migrat.zuul.provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

/**
 *Zuul服务熔断降级处理
 * 
 * @author: wangyi
 *
 */
@Component
public class ApiFallbackProvider implements FallbackProvider{

	@Override
	public String getRoute() {
		// 表名为哪个微服务提供回退机制，return "*" 代表为所有的微服务提供回退机制
		return "*";
	}

	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		
		return new ClientHttpResponse() {
			
			@Override
			public HttpHeaders getHeaders() {
				// 和body的编码内容保持一致
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
				return httpHeaders;
			}
			
			@Override
			public InputStream getBody() throws IOException {
				Map<String, Object> fallback = Maps.newHashMap();
				fallback.put("success", false);
				fallback.put("code", 408);
				fallback.put("message", "请求超时，请稍后再试！");
				fallback.put("data", null);
				return new ByteArrayInputStream(JSON.toJSONString(fallback).getBytes("UTF-8"));
			}
			
			@Override
			public String getStatusText() throws IOException {

				return HttpStatus.OK.getReasonPhrase();
			}
			
			@Override
			public HttpStatus getStatusCode() throws IOException {
				// 网关向API请求是失败的，但是消费者客户端向网关发起的请求是OK的，不应该把API的404,500等问题抛给客户
				// 网关和API集群对客户来说就是个黑盒子
				return HttpStatus.OK;
			}
			
			@Override
			public int getRawStatusCode() throws IOException {

				return HttpStatus.OK.value();
			}
			
			@Override
			public void close() {
				
			}
		};
	}

}
