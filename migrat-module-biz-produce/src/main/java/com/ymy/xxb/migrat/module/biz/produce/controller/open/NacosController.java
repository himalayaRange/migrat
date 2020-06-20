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
package com.ymy.xxb.migrat.module.biz.produce.controller.open;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.configuration.api.support.MigratNacosConfigSupport;
import com.ymy.xxb.migrat.configuration.properties.NacosProperties;
import com.ymy.xxb.migrat.module.biz.produce.constant.Const;
import com.ymy.xxb.migrat.module.biz.produce.constant.ModuleConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * Nacos 控制器测试
 * @author: wangyi
 *
 */
@RefreshScope
@RestController
@RequestMapping(Constants.API_OPEN_PREFIX +  ModuleConst.Access.API_MODULE_NACOS)
@Api(value = "Nacos API", tags = "Nacos", description = "Nacos注册中心和配置中心测试控制器")
public class NacosController {
	
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${spring.cloud.nacos.config.namespace}")
	private String namespace;
	
	@Value("${spring.cloud.nacos.config.server-addr}")
	private String serverAddr;
	
	@Autowired
	private MigratNacosConfigSupport migratNacosConfigSupport;

	/**
	 * Client Get Config.
	 * @return
	 */
	@GetMapping("/client/config/get")
	@ApiOperation(value = "Client Get Nacos Config" , httpMethod = "GET"  ,  notes = "Client Get Nacos Config" , produces = "application/json;charset=UTF-8")
	public SoulResult clientGetConfig() {
		try {
			NacosProperties nacosProperties = new NacosProperties();
			nacosProperties.setNamespace(namespace);
			nacosProperties.setServerAddr(serverAddr);
			nacosProperties.setDataId("migrat.properties");
			nacosProperties.setGroup("Common-Config");
			nacosProperties.setTimeout(3000L);
			String config = migratNacosConfigSupport.getConfig(nacosProperties);
			return SoulResult.success("client get config success", config);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("client get config fail ," + e.getMessage());
		}
	}
	
	/**
	 * Nacos Client Publish Config.
	 * @return
	 */
	@PostMapping("/client/config/publish")
	@ApiOperation(value = "Client Publish Nacos Config" , httpMethod = "POST"  ,  notes = "Client Publish Nacos Config" , produces = "application/json;charset=UTF-8")
	public SoulResult clientPublishConfig() {
		try {
			NacosProperties nacosProperties = new NacosProperties();
			nacosProperties.setNamespace(namespace);
			nacosProperties.setServerAddr(serverAddr);
			nacosProperties.setDataId("migrat.properties");
			nacosProperties.setGroup("Common-Config");
			nacosProperties.setTimeout(3000L);
			boolean isPublishOk= migratNacosConfigSupport.publishConfig(nacosProperties, "userLocalCache=false");
			return SoulResult.success("client publish config success", isPublishOk);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("client publish config fail ," + e.getMessage());
		}
	}
	
	/**
	 * 负载均衡方式获取唯一ID.
	 * loadBalanceClient和RestTemplate相结合方式访问，支持与Ribbon进行负载均衡
	 * @param request
	 * @return
	 */
	@GetMapping("/createGeneratorId")
	@ApiOperation(value = "Discovery Consumer Client echo" , httpMethod = "GET"  ,  notes = "Discovery Consumer Client echo" , produces = "application/json;charset=UTF-8")
	public String createGeneratorId(HttpServletRequest request) {
		ServiceInstance serviceInstance = loadBalancerClient.choose(Const.APPLICATION_NAME);
		String requestURL = String.format("http://%s:%s/%s", serviceInstance.getHost(), serviceInstance.getPort(), "openApi/sso/createGeneratorId");
		String resp = restTemplate.getForObject(requestURL, String.class);
		return resp;
	}
	
	/**
	 * 负载均衡方式获取在线用户数.
	 * loadBalanceClient和RestTemplate相结合方式访问，支持与Ribbon进行负载均衡
	 * @param request
	 * @return
	 */
	@PostMapping("/getAllOnlineUser")
	@ApiOperation(value = "Discovery Consumer Client getAllOnlineUser" , httpMethod = "POST"  ,  notes = "Discovery Consumer Client getAllOnlineUser" , produces = "application/json;charset=UTF-8")
	public String getAllOnlineUser(HttpServletRequest request) {
		ServiceInstance serviceInstance = loadBalancerClient.choose(Const.APPLICATION_NAME);
		String requestURL = String.format("http://%s:%s/%s", serviceInstance.getHost(), serviceInstance.getPort(), "openApi/sso/getAllOnlineUser");
		String resp = restTemplate.postForObject(requestURL, null, String.class);
		return resp;
	}
	
}
