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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Maps;
import com.ymy.xxb.migrat.auth.shiro.entity.BsMenuDO;
import com.ymy.xxb.migrat.auth.shiro.entity.BsRoleDO;
import com.ymy.xxb.migrat.auth.shiro.service.BsRoleService;
import com.ymy.xxb.migrat.auth.shiro.utils.ShiroContextUtil;
import com.ymy.xxb.migrat.auth.shiro.vo.MenuTree;
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
 * 角色管理器
 * @author: wangyi
 *
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.API.API_MODULE_BS_ROLE)
@Api(value = "roleController API", tags = "bsRole", description = "角色管理控制器")
public class BsRoleController {
	
	@Autowired
	private BsRoleService bsRoleService;
	
	@DS(Constants.AUTO_ROUTING)
	@GetMapping("/droplist")
	@ApiOperation(value = "获取角色下拉框" , httpMethod = "GET"  ,  notes = "获取角色下拉框" , produces = "application/json;charset=UTF-8")
	public SoulResult droplist(
			@ApiParam(name="keyword",value="角色名称关键字，支持模糊查询",required=false) @RequestParam(value="keyword",required=false) String keyword	
			) {
		List<Map<String,Object>> droplist = bsRoleService.droplist(keyword);
		return SoulResult.success("获取角色信息成功", droplist);
	}
	
	@RequiresPermissions("role:view")
	@GetMapping(value = "/select")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "角色编码", name = "roleCode",dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "角色名称", name = "roleName", dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "当前页数", name = "page", dataType = "Integer", paramType = "query", required = true),
		@ApiImplicitParam(value = "每页显示条数", name = "rows", dataType = "Integer", paramType = "query", required = true)
	})
	@DS(Constants.AUTO_ROUTING)
	@ApiOperation(value = "获取角色列表" , httpMethod = "GET"  ,  notes = "根据参数获取角色列表" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> param = PageParamUtil.pageParam(request);
			PageUtils<BsRoleDO> page = bsRoleService.selectPage(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.success("查询异常 ,ERROR:" + e.getMessage());
		}
	}
	
	@DS(Constants.AUTO_ROUTING)
	@RequiresPermissions("role:add")
	@PostMapping(value = "/insert")
	@ControllerEndpoint(operation = "新增角色" , exceptionMessage = "新增角色失败")
	@ApiOperation(value = "新增角色" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增角色信息,非必输项为冗余字段")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response, BsRoleDO bsRoleDO) {
		try {
			if (bsRoleDO == null || StringUtils.isEmpty(bsRoleDO.getRoleName())) {
				return SoulResult.warn("角色名称不能为空");
			}
			Map<String, Object> param = Maps.newHashMap();
			param.put("roleName", bsRoleDO.getRoleName());
			List<BsRoleDO> list = bsRoleService.select(param);
			if(list != null && list.size() > 0){
				return SoulResult.warn("当前角色已存在，请修改后再提交");
			}
			// TODO 生成角色编码
			//保存
			bsRoleDO.setCreateUser(ShiroContextUtil.currentUsername());
			int temp = bsRoleService.insert(bsRoleDO);
			if (temp > 0) {
				BsRoleDO role = bsRoleService.findById(bsRoleDO.getId());
				return SoulResult.success("新增成功", role);
			} else {
				return SoulResult.error("新增失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("新增异常,ERROR:" + e.getMessage());
		}
	}
	
	@DS(Constants.AUTO_ROUTING)
	@RequiresPermissions("role:update")
	@PostMapping(value = "/update")
	@ControllerEndpoint(operation = "修改角色" , exceptionMessage = "修改角色失败")
	@ApiOperation(value = "修改角色信息" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "修改角色信息,非必输项为冗余字段")
	public SoulResult update(HttpServletRequest request, HttpServletResponse response, BsRoleDO bsRoleDO) {
		try {
			if (bsRoleDO == null || StringUtils.isEmpty(bsRoleDO.getRoleName())
					) {
				return SoulResult.warn("角色名不能为空");
			}
			bsRoleDO.setModifyUser(ShiroContextUtil.currentUsername());
			int temp = bsRoleService.update(bsRoleDO);
			if (temp > 0) {
				return SoulResult.success("修改成功");
			} else {
				return SoulResult.error("修改失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("修改异常,ERROR:" + e.getMessage());
		}
	}
	
	
	@DS(Constants.AUTO_ROUTING)
	@GetMapping("/findById/{id}")
	@ApiOperation(value = "按ID查询", httpMethod="GET" , notes = "按用户ID查询角色信息" , produces = "application/json;charset=UTF-8")
	public SoulResult findById(
			@ApiParam(name="id",value="用户ID",required=true) @RequestParam(value="id",required=true) String id) {
		if(id == null){
			
			return SoulResult.warn("缺少参数：ID");
		}
		try {
			BsRoleDO bsUserDO =  bsRoleService.findById(id);
			
			return SoulResult.success("查询成功", bsUserDO);
		} catch (Exception e) {
			
			return SoulResult.error(e.getMessage());
		}
	}
	
	
	@DS(Constants.AUTO_ROUTING)
	@PostMapping("/assignPerms")
	@ControllerEndpoint(operation = "分配权限" , exceptionMessage = "分配权限失败")
	@ApiOperation(value = "分配权限", httpMethod="POST" , notes = "按角色分配菜单/按钮权限" , produces = "application/json;charset=UTF-8")
	public SoulResult assignPerms(
			@ApiParam(name="roleId",value="角色ID",required=true) @RequestParam(value="roleId",required=true) String roleId,
			@ApiParam(name="menuIds",value="菜单ID集合,多个ID之间用,隔开",required=true) @RequestParam(value="menuIds",required=true) String menuIds
			) {
		if (StringUtils.isEmpty(roleId)) {
			return SoulResult.warn("缺少角色id");
		}
		bsRoleService.assignPerms(roleId, menuIds);
		return SoulResult.success("分配角色成功");
	}
	
	
	@DS(Constants.AUTO_ROUTING)
	@GetMapping("/rolePermsTree")
	@ApiOperation(value = "获取角色的权限树", httpMethod="GET" , notes = "通过角色ID查询角色权限" , produces = "application/json;charset=UTF-8")
	public SoulResult rolePermsTree(
			@ApiParam(name="roleId",value="角色ID",required=true) @RequestParam(value="roleId",required=true) String roleId
			) {
		if (StringUtils.isEmpty(roleId)) {
			return SoulResult.warn("请输入角色ID");
		}
		MenuTree<BsMenuDO> menuTree = bsRoleService.getRolePermsTree(roleId);
		return SoulResult.success("获取角色权限成功", menuTree);
	}
	
	
	@DS(Constants.AUTO_ROUTING)
	@GetMapping("/rolePerms")
	@ApiOperation(value = "获取角色的权限数组", httpMethod="GET" , notes = "通过角色ID查询角色权限数组" , produces = "application/json;charset=UTF-8")
	public SoulResult rolePerms(
			@ApiParam(name="roleId",value="角色ID",required=true) @RequestParam(value="roleId",required=true) String roleId
			) {
		if (StringUtils.isEmpty(roleId)) {
			return SoulResult.warn("请输入角色ID");
		}
		 List<BsMenuDO> list =  bsRoleService.getRolePerms(roleId);
		 List<String> idList = new ArrayList<>();
		 for (BsMenuDO me : list) {
			 idList.add(me.getId());
		 }
		 String[] perms = new String[list.size()];
		return SoulResult.success("获取角色权限成功", idList.toArray(perms));
	}
	
	@DS(Constants.AUTO_ROUTING)
	@DeleteMapping("/delete")
	@RequiresPermissions("role:delete")
	@ApiOperation(value = "删除角色", httpMethod="DELETE" , notes = "通过ID删除角色" , produces = "application/json;charset=UTF-8")
	public SoulResult deleteRole(
			@ApiParam(name="id",value="角色ID",required=true) @RequestParam(value="id",required=true) String id
			) {
		if (StringUtils.isEmpty(id)) {
			return SoulResult.warn("缺少角色ID");
		}
		bsRoleService.removeRole(id);
		return SoulResult.success("角色删除成功");
	}
}
