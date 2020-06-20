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
package com.ymy.xxb.migrat.module.biz.produce.controller.auth;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.page.PageParamUtil;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.coodinator.register.client.MicroServiceRouteTenantClient;
import com.ymy.xxb.migrat.module.biz.produce.constant.ModuleConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 授权RPC调用DEMO
 *
 * @author: wangyi
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.Api.API_MODULE_RPC)
@Api(value = "AUTH RPC API", tags = "AuthRpc OpenFeign", description = "授权RPC调用DEMO控制器")
public class AuthRpcController {
	
	@Autowired
	private MicroServiceRouteTenantClient tenantClient;
	
	@GetMapping(value = "/comBox/user")
	@ApiOperation(value = "获取用户下拉框" , httpMethod = "GET"  ,  notes = "RPC获取用户下拉框" , produces = "application/json;charset=UTF-8")
	public SoulResult comBox(
			HttpServletRequest request,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "realName", required = false) String realName,
			@RequestParam(value = "mobile", required = false) String mobile
			) {
		Map<String, Object> param = PageParamUtil.pageParam(request);
		return tenantClient.comBox(param);
	}
	
	@PostMapping("/queryDictionaryDetail")
	@ApiOperation(value = "获取字典明细" , httpMethod = "POST" , notes = "根据字典表ID,获取字典明细对应信息")
	public SoulResult queryDictionaryDetail(
			@ApiParam(name="id", value="字典表主表ID",required = false)@RequestParam(value = "id", required = false) String id,
			@ApiParam(name="categoryCode", value="字典分类",required = false)@RequestParam(value = "categoryCode", required = false) String categoryCode
			) {
		return tenantClient.queryDictionaryDetail(id, categoryCode);
	}
	
	
	@PostMapping(value = "/lockBiz")
	@ApiOperation(value = "分布式锁业务操作" , httpMethod = "POST"  ,  notes = "支持重入锁，线程安全，分布式业务锁操作" , produces = "application/json;charset=UTF-8")
	public SoulResult lockBiz(
			@RequestParam(value = "lockKey", required = true) String lockKey
			) {
		return tenantClient.lockBiz(lockKey); 
	}

}
