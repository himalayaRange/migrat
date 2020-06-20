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
package com.ymy.xxb.migrat.coodinator.register.client;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.coodinator.register.config.FeiginConfig;
import com.ymy.xxb.migrat.coodinator.register.constant.Const;

/**
 * 租户权限模块微服务
 * 
 * @author: wangyi
 *
 */
@FeignClient(name = Const.SERVICE_ID_TENANT, configuration = FeiginConfig.class)
public interface MicroServiceRouteTenantClient {
	
	/**
	 * GET Remote Rpc generatorId
	 * @return
	 */
	@GetMapping("/openApi/sso/createGeneratorId")
	public SoulResult generatorId();
	
	/**
	 * GET Remote Rpc  createTenantInfo
	 * @return
	 */
	@GetMapping("/openApi/sso/createTenantInfo")
	public SoulResult tenantInfo(
				@RequestParam(value="companyId",required=true) String companyId,
				@RequestParam(value="tenantId",required=true) String tenantId
			);
	
	/**
	 * POST Remote Rpc credentials
	 * @return
	 */
	@PostMapping("/openApi/sso/getCredentials")
	public SoulResult credentials(
			    @RequestParam(value = "username", required = true) String username,
			    @RequestParam(value = "password", required = true) String password
			);
	
	/**
	 * POST Remote Rpc comBox
	 * 
	 * 注意，Map传参需要加@RequestParam注解，但不需要加注解的value参数
	 * 
	 * @param param
	 * @return
	 */
	@GetMapping(value = "/authenticationApi/comBox/rpc/user")
	public SoulResult comBox(
				@RequestParam() Map<String, Object> param
			);
	
	/**
	 * 通过ID或者分类查询字典详情
	 * @param id
	 * @param categoryCode
	 * @return
	 */
	@PostMapping(value = "/authenticationApi/dictionaryMain/queryDictionaryDetail")
	public SoulResult queryDictionaryDetail(
				@RequestParam(value = "id", required = false) String id,
				@RequestParam(value = "categoryCode", required = false) String categoryCode
			);
	
	
	/**
	 * 分布式锁操作
	 * @param lockKey
	 * @return
	 */
	@PostMapping(value = "/authenticationApi/rs/lockBiz")
	public SoulResult lockBiz(
			@RequestParam(value = "lockKey", required = true) String lockKey
			);
	
}
