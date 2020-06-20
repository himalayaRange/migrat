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
package com.ymy.xxb.migrat.zuul.policy;

import javax.servlet.http.HttpServletRequest;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimitKeyGenerator;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimitUtils;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.DefaultRateLimitKeyGenerator;

/**
 * Key Generator
 *
 * If the application needs to control the key strategy beyond the options
 * offered by the type property then it can be done just
 * 
 * by creating a custom RateLimitKeyGenerator implementation adding further
 * qualifiers or something entirely different
 * 
 * # https://github.com/marcosbarbero/spring-cloud-zuul-ratelimit
 * 
 * @author: wangyi
 *
 */
@Component
public class MyRateLimitKeyGenerator {

	@Bean
	public RateLimitKeyGenerator ratelimitKeyGenerator(RateLimitProperties properties, RateLimitUtils rateLimitUtils) {
		return new DefaultRateLimitKeyGenerator(properties, rateLimitUtils) {
			@Override
			public String key(HttpServletRequest request, Route route, RateLimitProperties.Policy policy) {
				return super.key(request, route, policy) + ":" + request.getMethod();
			}
		};
	}
}
