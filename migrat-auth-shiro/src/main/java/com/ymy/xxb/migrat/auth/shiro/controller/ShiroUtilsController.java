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
package com.ymy.xxb.migrat.auth.shiro.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ymy.xxb.migrat.auth.shiro.constant.ShiroModuleConst;
import com.ymy.xxb.migrat.auth.shiro.service.IdempotentTokenService;
import com.ymy.xxb.migrat.auth.shiro.utils.ShiroContextUtil;
import com.ymy.xxb.migrat.auth.shiro.vo.ProfileResultVo;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.common.utils.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 *
 * @author: wangyi
 *
 */
@RestController
@RequestMapping(Constants.API_OPEN_PREFIX + ShiroModuleConst.MODULE_SSO)
@Api(value = "Shiro Utils API", tags = "utils", description = "Shiro Utils")
public class ShiroUtilsController {
	@Autowired
	private RedisSessionDAO redisSessionDAO;
	
	@Autowired
	private IdempotentTokenService tokenService; 
	
	/**
	 * 生成分布式全局ID
	 * @return
	 */
	@GetMapping("/createGeneratorId")
	@ApiOperation(value = "生成分布式全局ID" , httpMethod = "GET"  ,  notes = "生成分布式全局ID" , produces = "application/json;charset=UTF-8")
	public SoulResult createGeneratorId() {
		IdWorker idWorker = new IdWorker();
		long nextId = idWorker.nextId();
		return SoulResult.success("ID生成成功", nextId);
	}
	
	
	/**
	 * 生成租户唯一标识
	 * @return
	 */
	@GetMapping("/createTenantInfo")
	@ApiOperation(value = "生成租户唯一的标识" , httpMethod = "GET"  ,  notes = "生成租户唯一的标识，登录URL中添加" , produces = "application/json;charset=UTF-8")
	public SoulResult createTenantInfo(
			@ApiParam(name="companyId",value="公司ID",required=true) @RequestParam(value="companyId",required=true) String companyId,
			@ApiParam(name="tenantId",value="租户ID",required=true) @RequestParam(value="tenantId",required=true) String tenantId
			) {
		String tenantCode = ShiroContextUtil.createSimpleTenantCode(companyId, tenantId);
		return SoulResult.success("租户唯一的标识生成成功", tenantCode);
	}
	
	/**
	 * 校验密码
	 * @param username
	 * @param password
	 * @return
	 */
	@PostMapping("/getCredentials")
	@ApiOperation(value = "秘钥生成" , httpMethod = "POST"  ,  notes = "秘钥生成" , produces = "application/json;charset=UTF-8")
	public SoulResult getCredentials(
			@ApiParam(name = "username", value = "用户名/手机号", required = true) @RequestParam(value = "username", required = true) String username,
			@ApiParam(name = "password", value = "密码", required = true) @RequestParam(value = "password", required = true) String password) {
		return SoulResult.success("生成成功", ShiroContextUtil.createGeneratorPassword(username, password));
	}
	
	/**
	 * 获取在线用户
	 * @return
	 */
	@PostMapping("/getAllOnlineUser")
	@ApiOperation(value = "获取在线用户" , httpMethod = "POST"  ,  notes = "获取在线用户" , produces = "application/json;charset=UTF-8")
	public SoulResult getAllOnlineUser() {
		Collection<Session> activeSessions = redisSessionDAO.getActiveSessions();
		List<Map<String, Object>> onlines = Lists.newArrayList();
		for (Session session : activeSessions) {
			Map<String, Object> iMap = Maps.newHashMap();
			SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
			if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
				continue;
			} else {
				principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				ProfileResultVo profileResultVo = (ProfileResultVo)principalCollection.getPrimaryPrincipal();
				iMap.put("username", profileResultVo.getUsername());
			}
			iMap.put("id", session.getId().toString());
			iMap.put("host", session.getHost());
			iMap.put("last_access_time", session.getLastAccessTime());
			iMap.put("start_time_stamp", session.getStartTimestamp());
			iMap.put("time_out", session.getTimeout());
			onlines.add(iMap);
		}
		
		return SoulResult.success("获取在线用户成功", onlines);
	}
	
	@PostMapping("/kicKoutUser/{sessionId}")
	@ApiOperation(value = "踢出指定用户" , httpMethod = "POST"  ,  notes = "踢出指定用户" , produces = "application/json;charset=UTF-8")
	public SoulResult kicKoutUser(
			@ApiParam(name="sessionId", value="sessionId",required=true ) @RequestParam(value = "sessionId", required = true) String sessionId
			) {
		Session session = redisSessionDAO.readSession(sessionId);
		redisSessionDAO.delete(session);
		
		return SoulResult.success("sessionId:" + sessionId + " 已被系统踢出");
	}
	
	
	@PostMapping("/hystrix/timeout")
	@ApiOperation(value = "Hystrix超时测试" , httpMethod = "POST"  ,  notes = "Hystrix超时测试" , produces = "application/json;charset=UTF-8")
	public SoulResult hystrixTest() throws InterruptedException {
		Thread.sleep(10000);
		return SoulResult.success("Hystrix Timeout测试成功");
	}
	
	/**
	 * Xxl Job
	 * @return
	 */
	@GetMapping("/job/xxl")
	@ApiOperation(value = "XXL JOB TEST" , httpMethod = "GET"  ,  notes = "XXL JOB TEST" , produces = "application/json;charset=UTF-8")
	public SoulResult xxl() {
		System.out.print("start execute job : xxl");
		
		return SoulResult.success("xxl job exec success");
	}
	
	@GetMapping("/idempotent/token")
	@ApiOperation(value = "Get IdempotentToken" , httpMethod = "GET"  ,  notes = "IdempotentToken" , produces = "application/json;charset=UTF-8")
	public SoulResult getIdempotentToken() {
		String accessToken = tokenService.createToken();
		return accessToken == null ? SoulResult.error("获取token失败！") : SoulResult.success("获取token成功！", accessToken);
	}
	
}
