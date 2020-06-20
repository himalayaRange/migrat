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
package com.ymy.xxb.migrat.module.website.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ymy.xxb.migrat.configuration.config.Swagger2AggregatConfig;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author: wangyi
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "swagger", name = "open", havingValue = "true")
public class Swagger2Config {
	
	@Autowired
	private Swagger2AggregatConfig swagger2AggregatConfig;
	
	/**
	 * 授权API
	 * @return
	 */
	@Bean(value = "websiteAuthApi")
	public Docket authApi() {
		return swagger2AggregatConfig.authWithWebsite();
	}
	
	/**
	 * 开放API
	 */
	@Bean(value = "websiteOpenApi")
	public Docket openApi() {
		return swagger2AggregatConfig.openWithWebsite();
	}

}
