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
package com.ymy.xxb.migrat.module.comyany.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.page.PageParamUtil;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.comyany.constant.ModuleConst;
import com.ymy.xxb.migrat.module.comyany.service.ComBoxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * 租户模块下拉框控制器
 * 
 * @author: wangyi
 *
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.API.API_MODULE_COM_BOX)
@Api(value = "ComBoxController API", tags = "comBox", description = "租户模块下拉框控制器")
public class ComBoxController { 
	
	@Autowired
	private ComBoxService comBoxService;
	
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping("/comBox/user")
	@ApiOperation(value = "查询用户列表下拉框", httpMethod="GET" , notes = "查询用户列表下拉框" , produces = "application/json;charset=UTF-8")
	public SoulResult userComBox(
			HttpServletRequest request,
			@ApiParam(name = "username", value = "用户名", required = false) @RequestParam(value = "username", required = false) String username,
			@ApiParam(name = "realName", value = "真实姓名", required = false) @RequestParam(value = "realName", required = false) String realName,
			@ApiParam(name = "mobile", value = "手机号", required = false) @RequestParam(value = "mobile", required = false) String mobile
			) {
		Map<String, Object> param = PageParamUtil.pageParam(request);
		List<Map<String, Object>> comBox = comBoxService.userComBox(param);
		return SoulResult.success("请求成功", comBox);
	}
	
	/**
	 * RPC调用
	 * 
	 * 注意: 需要加@RequestParam注解，但不需要加注解的value参数
	 * @param param
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping("/rpc/user")
	public SoulResult rpcUserComBox(
			@RequestParam() Map<String, Object> param
			) {
		List<Map<String, Object>> comBox = comBoxService.userComBox(param);
		return SoulResult.success("请求成功", comBox);
	}
	
}
