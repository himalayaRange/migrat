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
package com.ymy.xxb.migrat.configuration.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.google.common.collect.Maps;
import com.ymy.xxb.migrat.configuration.constant.Const;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger2 文档聚合
 * 
 * <p>单项目引用和网关聚合</p>
 *
 * @author: wangyi
 *
 */
@Component
public class Swagger2AggregatConfig {
	
	/**
	 * 聚合项目中所有的文档
	 * @return
	 */
	public Map<String, Docket> swagger2Gateway() {
		Map<String,Docket> dockerMaps = Maps.newHashMap();
		dockerMaps.put("website-auth", authWithWebsite());
		dockerMaps.put("website-open", openWithWebsite());
		dockerMaps.put("tenant-auth", authWithTenant());
		dockerMaps.put("tenant-open", openWithTenant());
		dockerMaps.put("bizProduce-auth", authWithBizProduce());
		dockerMaps.put("bizProduce-open", openWithBizProduce());
		return dockerMaps;
	}
	
	/**
	 * website授权API
	 * @return
	 */
	public Docket authWithWebsite() {
		ParameterBuilder tokenPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<Parameter>();
		tokenPar.name(Const.HEAFER_SESSION_ID).description("访问令牌").modelRef(new ModelRef("string")).parameterType("header").required(true).build();
		pars.add(tokenPar.build());
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("YMY.XXB.MIGRAT.DEV")
				.apiInfo(
				    new ApiInfoBuilder().title("MIGRAT SAAS 官网 API 接口文档").description("migrat-module-website swagger2 api")
						.termsOfServiceUrl( Const.SWAGGER2_WEBSITE_SERVICE_URL ).version("1.0")
						.contact(new Contact("WANG.YI", Const.SWAGGER2_WEBSITE_SERVICE_URL, "13127636621@163.com")).build()
				)
				.select()
				// 设置basePackage会将包下的所有被@Api标记类的所有方法作为api
				.apis(RequestHandlerSelectors.basePackage(Const.SCAN_WEBSITE_CONTR_AUTH_PACKAGE))
				// 只有标记了@ApiOperation的方法才会暴露出给swagger
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any())
				.build()
				.groupName("官网模块-授权API")
				.globalOperationParameters(pars);
		
	}
	
	/**
	 * website开放APPI
	 * @return
	 */
	public Docket openWithWebsite() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("YMY.XXB.MIGRAT.DEV")
				.apiInfo(
						new ApiInfoBuilder().title("MIGRAT SAAS 官网 API 接口文档").description("migrat-module-website swagger2 api")
						.termsOfServiceUrl( Const.SWAGGER2_WEBSITE_SERVICE_URL ).version("1.0")
						.contact(new Contact("WANG.YI", Const.SWAGGER2_WEBSITE_SERVICE_URL, "13127636621@163.com")).build()
				)
				.select()
				// 设置basePackage会将包下的所有被@Api标记类的所有方法作为api
				.apis(RequestHandlerSelectors.basePackage(Const.SCAN_WEBSITE_CONTR_OPEN_PACKAGE))
				// 只有标记了@ApiOperation的方法才会暴露出给swagger
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any())
				.build()
				.groupName("官网模块-开放API");
	}
	
	
	/**
	 * Biz Produce授权API
	 * @return
	 */
	public Docket authWithBizProduce() {
		ParameterBuilder tokenPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<Parameter>();
		tokenPar.name(Const.HEAFER_SESSION_ID).description("访问令牌").modelRef(new ModelRef("string")).parameterType("header").required(true).build();
		pars.add(tokenPar.build());
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("YMY.XXB.MIGRAT.DEV")
				.apiInfo(
				    new ApiInfoBuilder().title("MIGRAT SAAS 业务模块[生产] API 接口文档").description("migrat-module-biz-produce swagger2 api")
						.termsOfServiceUrl( Const.SWAGGER2_BIZ_PRODUCE_SERVICE_URL).version("1.0")
						.contact(new Contact("WANG.YI", Const.SWAGGER2_BIZ_PRODUCE_SERVICE_URL, "13127636621@163.com")).build()
				)
				.select()
				// 设置basePackage会将包下的所有被@Api标记类的所有方法作为api
				.apis(RequestHandlerSelectors.basePackage(Const.SCAN_BIZ_PRODUCE_CONTR_AUTH_PACKAGE))
				// 只有标记了@ApiOperation的方法才会暴露出给swagger
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any())
				.build()
				.groupName("业务-生产模块-授权API")
				.globalOperationParameters(pars);
		
	}
	
	
	/**
	 * Biz Produce开放APPI
	 * @return
	 */
	public Docket openWithBizProduce() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("YMY.XXB.MIGRAT.DEV")
				.apiInfo(
						new ApiInfoBuilder().title("MIGRAT SAAS 业务模块[生产] API 接口文档").description("migrat-module-biz-produce swagger2 api")
						.termsOfServiceUrl( Const.SWAGGER2_BIZ_PRODUCE_SERVICE_URL ).version("1.0")
						.contact(new Contact("WANG.YI", Const.SWAGGER2_BIZ_PRODUCE_SERVICE_URL, "13127636621@163.com")).build()
				)
				.select()
				// 设置basePackage会将包下的所有被@Api标记类的所有方法作为api
				.apis(RequestHandlerSelectors.basePackage(Const.SCAN_BIZ_PRODUCE_CONTR_OPEN_PACKAGE))
				// 只有标记了@ApiOperation的方法才会暴露出给swagger
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any())
				.build()
				.groupName("业务生产模块-开放API");
	}
	
	
	/**
	 * Tenant授权API
	 * @return
	 */
	public Docket authWithTenant() {
		ParameterBuilder tokenPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<Parameter>();
		tokenPar.name(Const.HEAFER_SESSION_ID).description("访问令牌").modelRef(new ModelRef("string")).parameterType("header").required(true).build();
		pars.add(tokenPar.build());
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("YMY.XXB.MIGRAT.DEV")
				.apiInfo(
				    new ApiInfoBuilder().title("MIGRAT SAAS 租户模块 API 接口文档").description("migrat-module-tenant swagger2 api")
						.termsOfServiceUrl( Const.SWAGGER2_TENANT_SERVICE_URL ).version("1.0")
						.contact(new Contact("WANG.YI", Const.SWAGGER2_TENANT_SERVICE_URL, "13127636621@163.com")).build()
				)
				.select()
				// 设置basePackage会将包下的所有被@Api标记类的所有方法作为api
				.apis(RequestHandlerSelectors.basePackage(Const.SCAN_TENANT_CONTR_AUTH_PACKAGE))
				// 只有标记了@ApiOperation的方法才会暴露出给swagger
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any())
				.build()
				.groupName("租户模块-授权API")
				.globalOperationParameters(pars);
		
	}
	
	/**
	 * Tenant开放APPI
	 * @return
	 */
	public Docket openWithTenant() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("YMY.XXB.MIGRAT.DEV")
				.apiInfo(
						new ApiInfoBuilder().title("MIGRAT SAAS 租户模块 API 接口文档").description("migrat-module-tenant swagger2 api")
						.termsOfServiceUrl( Const.SWAGGER2_TENANT_SERVICE_URL ).version("1.0")
						.contact(new Contact("WANG.YI", Const.SWAGGER2_TENANT_SERVICE_URL, "13127636621@163.com")).build()
				)
				.select()
				// 设置basePackage会将包下的所有被@Api标记类的所有方法作为api
				.apis(RequestHandlerSelectors.basePackage(Const.SCAN_TENANT_CONTR_OPEN_PACKAGE))
				// 只有标记了@ApiOperation的方法才会暴露出给swagger
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any())
				.build()
				.groupName("租户模块-开放API");
	}
}
