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
package com.ymy.xxb.migrat.gateway.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import com.ymy.xxb.migrat.configuration.config.Swagger2AggregatConfig;
import lombok.AllArgsConstructor;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * springcloud gateway 聚合 Swagger2文档
 *
 * @author: wangyi
 *
 */
@Primary
@Component
@AllArgsConstructor
public class GatewaySwagger2Provider implements SwaggerResourcesProvider {

	public static final String API_URI = "/v2/api-docs";

	private GatewayProperties gatewayProperties;

	private RouteLocator routeLocator;

	@Autowired
	private Swagger2AggregatConfig swagger2AggregatConfig;

	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<>();
		List<String> routes = new ArrayList<>();
		routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
		gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId()))
				.forEach(routeDefinition -> routeDefinition.getPredicates().stream()
						.filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
						.forEach(predicateDefinition -> {
							Map<String, Docket> dockets = swagger2AggregatConfig.swagger2Gateway();
							dockets.forEach((k, v) -> {
								if (k.contains(routeDefinition.getId())) {
									resources.add(swaggerResource(routeDefinition.getId() + v.getGroupName(),
											predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
													.replace("**", "v2/api-docs?group=" + v.getGroupName()),
											"1.0"));
								}
							});
						}));
		return resources;
	}

	private SwaggerResource swaggerResource(String name, String location, String version) {
		SwaggerResource swagger = new SwaggerResource();
		swagger.setName(name);
		swagger.setLocation(location);
		swagger.setSwaggerVersion(version);
		return swagger;
	}

}
