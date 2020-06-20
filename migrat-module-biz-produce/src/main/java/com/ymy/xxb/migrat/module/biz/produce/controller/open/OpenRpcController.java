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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.coodinator.register.client.MicroServiceRouteTenantClient;
import com.ymy.xxb.migrat.module.biz.produce.constant.ModuleConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * 开放微服务远程RPC调用DEMO
 * 
 * @author: wangyi
 *
 */
@RestController
@RequestMapping(Constants.API_OPEN_PREFIX +  ModuleConst.Access.API_MODULE_RPC)
@Api(value = "Remote RPC API DEMO", tags = "OpenRpc OpenFeign", description = "微服务远程RPC调用测试")
public class OpenRpcController {
	
	@Autowired
	private MicroServiceRouteTenantClient tenantClient;
	
	/**
	 * RPC 获取全局ID
	 * @return
	 */
	@GetMapping("/rpc/generatorId")
	@ApiOperation(value = "MicoService Coordinator Register With FeignClient Test, Support Ribbon LoadBalance" , httpMethod = "GET"  ,  notes = "RPC 获取全局ID" , produces = "application/json;charset=UTF-8")
	public SoulResult generatorId(
			) {
		SoulResult soulResult = tenantClient.generatorId();
		return soulResult;
	}
	
	/**
	 * RPC 生成租户唯一标识
	 * @return
	 */
	@GetMapping("/rpc/tenantInfo")
	@ApiOperation(value = "MicoService Coordinator Register With FeignClient Test, Support Ribbon LoadBalance" , httpMethod = "GET"  ,  notes = "RPC 生成租户唯一标识" , produces = "application/json;charset=UTF-8")
	public SoulResult tenantInfo(
			@ApiParam(name="companyId",value="公司ID",required=true) @RequestParam(value="companyId",required=true) String companyId,
			@ApiParam(name="tenantId",value="租户ID",required=true) @RequestParam(value="tenantId",required=true) String tenantId
			) {
		SoulResult soulResult = tenantClient.tenantInfo(companyId, tenantId);
		return soulResult;
	}
	
	
	/**
	 * RPC 秘钥生成
	 * @return
	 */
	@PostMapping("/rpc/credentials")
	@ApiOperation(value = "MicoService Coordinator Register With FeignClient Test, Support Ribbon LoadBalance" , httpMethod = "POST"  ,  notes = "RPC 秘钥生成", produces = "application/json;charset=UTF-8")
	public SoulResult credentials(
			@ApiParam(name = "username", value = "用户名/手机号", required = true) @RequestParam(value = "username", required = true) String username,
			@ApiParam(name = "password", value = "密码", required = true) @RequestParam(value = "password", required = true) String password
			) {
		SoulResult soulResult = tenantClient.credentials(username, password);
		return soulResult;
	}
	
}
