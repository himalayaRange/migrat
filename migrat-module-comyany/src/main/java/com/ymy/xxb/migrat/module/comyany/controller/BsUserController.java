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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.ymy.xxb.migrat.auth.shiro.constant.ShiroConst;
import com.ymy.xxb.migrat.auth.shiro.entity.BsUserDO;
import com.ymy.xxb.migrat.auth.shiro.realm.ShiroHelper;
import com.ymy.xxb.migrat.auth.shiro.service.BsUserService;
import com.ymy.xxb.migrat.auth.shiro.utils.ShiroContextUtil;
import com.ymy.xxb.migrat.common.annotation.ControllerEndpoint;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.page.PageParamUtil;
import com.ymy.xxb.migrat.common.page.PageUtils;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.comyany.constant.ModuleConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 *
 * 用户管理器
 * @author: wangyi
 *
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.API.API_MODULE_BS_USER)
@Api(value = "companyController API", tags = "bsUser", description = "企业用户管理控制器")
public class BsUserController {
	
	@Autowired
	private BsUserService bsUserService;
	
	@Autowired
	private ShiroHelper shiroHelper;
	
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping("/profile")
	@ApiOperation(value = "获取当前登录用户信息[角色][权限]" , httpMethod = "GET"  ,  notes = "根据参数获取企业用户列表" , produces = "application/json;charset=UTF-8")
	public SoulResult profile() {
		String currentUserId = ShiroContextUtil.currentUserId();
		BsUserDO bsUserDO = bsUserService.findById(currentUserId);
		AuthorizationInfo authorizationInfo = shiroHelper.getCurrentuserAuthorizationInfo();
		Map<String, Object> profile = Maps.newHashMap();
		profile.put("user", bsUserDO);
		profile.put(ShiroConst.PROFILE_ROLES, authorizationInfo.getRoles());
		profile.put(ShiroConst.PROFILE_PERMS, authorizationInfo.getStringPermissions());
		return SoulResult.success("获取用户信息成功", profile);
	}
	
	@DS(value = Constants.AUTO_ROUTING)
	@RequiresPermissions("user:view")
	@GetMapping(value = "/select")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "用户ID", name = "id",dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "用户名", name = "username", dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "手机号", name = "mobile",dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "当前页数", name = "page", dataType = "Integer", paramType = "query", required = true),
		@ApiImplicitParam(value = "每页显示条数", name = "rows", dataType = "Integer", paramType = "query", required = true)
	})
	@ApiOperation(value = "获取企业用户信息列表" , httpMethod = "GET"  ,  notes = "根据参数获取企业用户列表" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> param = PageParamUtil.pageParam(request);
			PageUtils<BsUserDO> page = bsUserService.selectPage(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.success("查询异常 ,ERROR:" + e.getMessage());
		}
	}
	
	
	@DS(value = Constants.AUTO_ROUTING)
	@RequiresPermissions("user:add")
	@PostMapping(value = "/insert")
	@ControllerEndpoint(operation = "新增用户", exceptionMessage = "新增用户失败")
	@ApiOperation(value = "新增用户" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增用户信息,非必输项为冗余字段")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response, @Valid BsUserDO bsUserDO) {
		try {
			if (bsUserDO == null 
					|| StringUtils.isEmpty(bsUserDO.getMobile())
					|| StringUtils.isEmpty(bsUserDO.getUsername())
					) {
				return SoulResult.warn("用户名、手机号不能为空");
			}
			// 设置用户所属公司
			bsUserDO.setCompanyId(ShiroContextUtil.currentUserCompanyId());
			// 用户等级赋值
			String userId = ShiroContextUtil.currentUserId();
			BsUserDO userDO = bsUserService.findById(userId);
			String level = userDO == null ? null : userDO.getLevel();
			if (level != null) {
				if (ShiroConst.BS_USER_LEVEL_SAASADMIN.equals(level)) {
					// 超级管理员只能添加租户管理员
					bsUserDO.setLevel(ShiroConst.BS_USER_LEVEL_COADMIN);
				} else if (ShiroConst.BS_USER_LEVEL_COADMIN.equals(level)) {
					// 公司管理员只能添加普通用户
					bsUserDO.setLevel(ShiroConst.BS_USER_LEVEL_USER);
				} else if (ShiroConst.BS_USER_LEVEL_USER.equals(level)) {
					// 普通用户如果被分配了添加用户的权限，那么他添加的用户也只能是普通用户级别
					bsUserDO.setLevel(ShiroConst.BS_USER_LEVEL_USER);
				} else {
					return SoulResult.warn("添加用户失败，用户信息等级异常，请联系系统管理员！");
				}
			} else {
				return SoulResult.warn("添加用户失败，未获取到用户信息");
			}
			Map<String, Object> param = Maps.newHashMap();
			param.put("mobile", bsUserDO.getMobile());
			List<BsUserDO> list = bsUserService.select(param);
			if(list != null && list.size() > 0){
				return SoulResult.warn("当前手机号和用户名已存在，请修改后再提交");
			}
			//保存
			bsUserDO.setCreateUser(ShiroContextUtil.currentUsername());
			bsUserDO.setPassword(ShiroContextUtil.createGeneratorPassword(bsUserDO.getUsername(), ShiroConst.SYSTEM_INITIAL_PASSWORD)); // 设置默认密码1234qwer
			bsUserService.addUser(bsUserDO);
			return SoulResult.success("新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("新增异常,ERROR:" + e.getMessage());
		}
	}
	
	
	@DS(value = Constants.AUTO_ROUTING)
	@RequiresPermissions("user:update")
	@PostMapping(value = "/update")
	@ControllerEndpoint(operation = "修改用户", exceptionMessage = "修改用户失败")
	@ApiOperation(value = "修改用户信息" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "修改用户信息,非必输项为冗余字段")
	public SoulResult update(HttpServletRequest request, HttpServletResponse response, @Valid BsUserDO bsUserDO) {
		try {
			if (bsUserDO == null
					|| StringUtils.isEmpty(bsUserDO.getMobile())
					|| StringUtils.isEmpty(bsUserDO.getUsername())
					) {
				return SoulResult.warn("手机号、用户名、所属公司不能为空");
			}
			bsUserDO.setPassword(null);
			bsUserDO.setModifyUser(ShiroContextUtil.currentUsername());
			bsUserService.modiftyUser(bsUserDO);
			return SoulResult.success("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("修改异常,ERROR:" + e.getMessage());
		}
	}
	
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping("/findById/{id}")
	@ApiOperation(value = "按ID查询", httpMethod="GET" , notes = "按用户ID查询用户信息" , produces = "application/json;charset=UTF-8")
	public SoulResult findById(
			@ApiParam(name="id",value="用户ID",required=true) @RequestParam(value="id",required=true) String id) {
		if(id == null){
			
			return SoulResult.warn("缺少参数：ID");
		}
		try {
			BsUserDO bsUserDO =  bsUserService.findById(id);
			
			return SoulResult.success("查询成功", bsUserDO);
		} catch (Exception e) {
			
			return SoulResult.error(e.getMessage());
		}
	}
	
	
	/**
	 * 密码修改，只有当前用户修改自己的密码
	 * @param id
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@PostMapping("/password/update")
	@ControllerEndpoint(operation = "修改密码" , exceptionMessage = "修改密码失败")
	@ApiOperation(value = "修改密码", httpMethod="POST" , notes = "修改用户密码" , produces = "application/json;charset=UTF-8")
	public SoulResult updatePassword(
			@ApiParam(name="id", value="用户ID", required=true) @RequestParam(value="id",required=true) String id,
			@ApiParam(name="oldPassword", value="旧密码", required=true) @RequestParam(value="oldPassword",required=true) String oldPassword,
			@ApiParam(name="newPassword", value="新密码", required=true) @RequestParam(value="newPassword",required=true) String newPassword
			) {
		if (       StringUtils.isEmpty(id)
				|| StringUtils.isEmpty(oldPassword) 
				|| StringUtils.isEmpty(newPassword)
			) {
			return SoulResult.warn("参数不能为空");
		}
		if (!id.equals(ShiroContextUtil.currentUserId())) {
			return SoulResult.warn("您不能修改非当前用户，您可以：【退出登录】-> 【重新登录】->【修改密码】");
		}
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", id);
		BsUserDO bsUserDO = bsUserService.getUserSimpleInfo(param);
		if (bsUserDO == null) {
			return SoulResult.warn("未查询到当前用户");
		}
		String username = bsUserDO.getUsername();
		String resourcePassword = bsUserDO.getPassword();
		String rightPassword = ShiroContextUtil.createGeneratorPassword(username, oldPassword);
		if (!Objects.equal(resourcePassword, rightPassword)) {
			return SoulResult.warn("旧密码错误");
		}
		String currPassword = ShiroContextUtil.createGeneratorPassword(username, newPassword);
		BsUserDO user = new BsUserDO();
		user.setId(bsUserDO.getId());
		user.setPassword(currPassword);
		user.setModifyUser(ShiroContextUtil.currentUsername()); // 修改人
		int result = bsUserService.update(user);
		if (result == 1) {
			// SecurityUtils.getSubject().logout();
			return SoulResult.success("密码修改成功,需要重新登录");
		} else {
			return SoulResult.error("密码修改失败");
		}
	}
	
	/**
	 * 密码重置：一般只有管理员有权限
	 * @param id
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@RequiresPermissions("user:password:reset")
	@PostMapping("/password/reset")
	@ControllerEndpoint(operation = "重置密码" , exceptionMessage = "重置密码失败")
	@ApiOperation(value = "重置密码", httpMethod="POST" , notes = "重置密码" , produces = "application/json;charset=UTF-8")
	public SoulResult resetPassword(
			@ApiParam(name="id", value="用户ID", required=true) @RequestParam(value="id",required=true) String id
			) {
		if (StringUtils.isEmpty(id)) {
			return SoulResult.warn("参数不能为空");
		}
		BsUserDO bsUserDO = bsUserService.findById(id);
		if (bsUserDO == null) {
			return SoulResult.warn("未查询到当前用户");
		}
		String username = bsUserDO.getUsername();
		String initPassword = ShiroContextUtil.createGeneratorPassword(username, ShiroConst.SYSTEM_INITIAL_PASSWORD);
		BsUserDO user = new BsUserDO();
		user.setId(bsUserDO.getId());
		user.setPassword(initPassword);
		user.setModifyUser(ShiroContextUtil.currentUsername());
		int result = bsUserService.update(user);
		if (result == 1) {
			return SoulResult.success("密码重置成功");
		} else {
			return SoulResult.error("密码重置失败");
		}
	}
	
	@DS(value = Constants.AUTO_ROUTING)
	@RequiresPermissions("user:delete")
	@DeleteMapping("/deleteUser")
	@ControllerEndpoint(operation = "删除用户" , exceptionMessage = "删除用户失败")
	@ApiOperation(value = "删除用户", httpMethod="DELETE" , notes = "删除用户" , produces = "application/json;charset=UTF-8")
	public SoulResult deleleUser(
			@ApiParam(name="id", value="用户ID", required=true) @RequestParam(value="id",required=true) String id
			) {
		if (StringUtils.isEmpty(id)) {
			return SoulResult.warn("参数不能为空");
		}
		bsUserService.deleleUser(id);
		return SoulResult.success("删除用户成功");
	}
	
}
