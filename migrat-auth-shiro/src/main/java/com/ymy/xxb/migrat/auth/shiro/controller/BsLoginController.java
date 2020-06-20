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

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.wf.captcha.base.Captcha;
import com.ymy.xxb.migrat.auth.shiro.constant.ShiroModuleConst;
import com.ymy.xxb.migrat.auth.shiro.entity.CompanyDO;
import com.ymy.xxb.migrat.auth.shiro.realm.MyShiroRealm;
import com.ymy.xxb.migrat.auth.shiro.service.CompanyService;
import com.ymy.xxb.migrat.auth.shiro.utils.CaptchaUtil;
import com.ymy.xxb.migrat.auth.shiro.utils.ShiroContextUtil;
import com.ymy.xxb.migrat.common.config.DataSourceContextHolder;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.common.utils.DbContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
/**
 * 登陆授权控制器
 *  
 * @author wangyi
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_OPEN_PREFIX + ShiroModuleConst.MODULE_SSO)
@Api(value = "Login API", tags = "login", description = "开放接口-登录模块")
public class BsLoginController {
	
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private CaptchaUtil captchaUtil;
	
	@Autowired
	private MyShiroRealm myShiroRealm;
	
	
	@GetMapping("/loginPage")
	@ApiOperation(value = "获取登录页信息" , httpMethod = "GET"  ,  notes = "通过参数from获取所属公司" , produces = "application/json;charset=UTF-8")
	public SoulResult LoginPage(
			@ApiParam(name = "from", value = "租户来源码", required = true) @RequestParam(value = "from", required = true) String from
			) {
		if (StringUtils.isEmpty(from)) {
			return SoulResult.warn("参数from不能为空");
		} else {
			// 查询所有租户信息
			List<CompanyDO> list = companyService.queryTenantInfo();
			// 是否找到公司
			boolean isFind = false;
			String companyName = "";
			for (CompanyDO item : list) {
				String tenantCode = ShiroContextUtil.createSimpleTenantCode(item.getId(), item.getTenantId());
				if (from.equals(tenantCode)) {
					isFind = true;
					companyName = item.getName();
					break;
				} else {
					continue;
				}
			}
			
			if (isFind) {
				return SoulResult.success("页面初始化成功", companyName);
			} else {
				return SoulResult.warn("from参数不正确，拒绝请求");
			}
		}
	}
	
	@PostMapping("/login")
	@ApiOperation(value = "系统登录" , httpMethod = "POST"  ,  notes = "用户名密码登陆" , produces = "application/json;charset=UTF-8")
	public SoulResult login(HttpServletRequest request,
			//@ApiParam(name = "remeberme", value = "记住我", required = false )@RequestParam(value = "remeberme" , required = false) String remeberme,
			@ApiParam(name = "from", value = "租户来源码，测试账户：2ED1CE0C3B94B7BF9E92F8A397A28978", required = true) @RequestParam(value = "from", required = true) String from,
			@ApiParam(name = "username", value = "手机号", required = true ) @RequestParam(value = "username", required = true) String username,
			@ApiParam(name = "password", value = "密码", required = true) @RequestParam(value = "password", required = true) String password,
			@ApiParam(name = "verifyCode", value = "验证码", required = false) @RequestParam(value = "verifyCode", required = false) String verifyCode
			) {
		if (StringUtils.isEmpty(from)) {
			return SoulResult.warn("缺少FROM参数");
		} else if(	StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return SoulResult.warn("请输入用户名和密码!");
		} else {
			 if (!captchaUtil.verify(verifyCode, request)) {
	            return SoulResult.warn("验证码错误或者已失效!");
	          }
			Subject subject = SecurityUtils.getSubject();
			if(subject.isAuthenticated()) {
				// 如果是当前用户则无需登录
				String currentMobile = ShiroContextUtil.currentMobile();
				if (username.equals(currentMobile)) {
					return SoulResult.success("已经登录，无需重新登录", subject.getSession().getId());
				} else {
					// 如果登录其他用户则先清除缓存再继续登录
					myShiroRealm.clearCache();
				}
			}
			// redis获取用户所属的库
			// boolean isFind = routingCurrentUserFromRedis(username);
			boolean isFind = routingCurrentUserFromLoginPage(from);
			
			if (!isFind) {return SoulResult.warn("用户名或密码错误!");}
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			//boolean remeberMe = remeberme == null ? false : true;
			//token.setRememberMe(remeberMe);
			try {
				subject.login(token);
				//if (remeberMe) {
				//	log.info("用户【{}】自动登陆---{}" , username , String.valueOf(System.currentTimeMillis()));
				//}
				String sessionID = (String)subject.getSession().getId();
				
				return SoulResult.success("登陆成功！", sessionID);
			} catch (AuthenticationException e) {
				log.warn("登陆失败，INFO:" + e.getMessage());
				return SoulResult.warn("用户名或密码错误!");
			} catch (Exception e) {
				e.printStackTrace();
				log.error("系统异常，INFO:" + e.getMessage());
				return SoulResult.error("系统出小差啦，请稍后再试，INFO:" + e.getMessage());
			} finally {
				// 清除路由信息
				clearRoutingCurrentUser();
			}
		}
	}
	
	@GetMapping("/logout")
	@ApiOperation(value = "系统退出" , httpMethod = "GET"  ,  notes = "退出系统登录" , produces = "application/json;charset=UTF-8")
	public SoulResult logout(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		clearRoutingCurrentUser();
		return SoulResult.success("登出成功！"); 
	}
	
	/**
	 * 生成验证码
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@GetMapping(value = "images/captcha", produces = MediaType.IMAGE_JPEG_VALUE)
	@ApiOperation(value = "生成验证码" , httpMethod = "GET"  ,  notes = "生成验证码" , produces = "application/json;charset=UTF-8")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		captchaUtil.outPng(110, 34, 4, Captcha.TYPE_ONLY_NUMBER, request, response);
	}
	
	/**
	 * 从Redis中获取路由信息
	 */
	private boolean routingCurrentUserFromRedis(String usernameOrMobile) {
		Integer tenantIdGroupUnit = Constants.TENANT_ID_GROUP_UNIT;
		String maxTenantId = companyService.maxTenantId();
		Integer max = Integer.valueOf(maxTenantId);
		// 判断当前系统中业务子系统一共多少个并循环
		Integer dbCount = (max / tenantIdGroupUnit) + 1;
		boolean isFind = false;
		for (int i = 1 ; i <= dbCount ; i ++) {
			Map<Object, Object> entries = redisTemplate.opsForHash().entries(Constants.CACHE_USERS_TENANT_ROUTING + "_" + String.valueOf(i));
			if (entries.containsKey(usernameOrMobile)) {
				DataSourceContextHolder.setTenantContext(DbContextUtil.getBizDbName(i));
				isFind = true;
				break;
			}
			continue;
		}
		return isFind;
	}
	
	
	/**
	 * 通过参数from进行路由
	 * @param from
	 * @return
	 */
	private boolean routingCurrentUserFromLoginPage(String from) {
		// 查询所有租户信息
		List<CompanyDO> list = companyService.queryTenantInfo();
		// 是否找到公司
		boolean isFind = false;
		for (CompanyDO item : list) {
			String tenantCode = ShiroContextUtil.createSimpleTenantCode(item.getId(), item.getTenantId());
			if (from.equals(tenantCode)) {
				isFind = true;
				DataSourceContextHolder.setTenantContext(DbContextUtil.getBizNameByTenantId(item.getTenantId()));
				break;
			} else {
				continue;
			}
		}
		return isFind;
	}
	
	/**
	 * 登录成功后清除路由信息
	 */
	private void clearRoutingCurrentUser() {
		DataSourceContextHolder.clearTenantContext();
	}
	
}
