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
import com.ymy.xxb.migrat.auth.shiro.service.BsMenuService;
import com.ymy.xxb.migrat.auth.shiro.utils.ShiroContextUtil;
import com.ymy.xxb.migrat.auth.shiro.vo.MenuTree;
import com.ymy.xxb.migrat.common.annotation.ControllerEndpoint;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.comyany.constant.ModuleConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 *
 * @author: wangyi
 *
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.API.API_MODULE_BS_MENU)
@Api(value = "MenuController API", tags = "bsMenu", description = "菜单管理控制器")
public class BsMenuController {
	
	@Autowired
	private BsMenuService bsMenuService;
	
	@DS(Constants.AUTO_ROUTING)
	@GetMapping("/currentUserMenuTree/{userId}")
	@ApiOperation(value = "查询当前用户菜单树" , httpMethod = "GET"  ,  notes = "获取当前用户菜单树" , produces = "application/json;charset=UTF-8")
	public SoulResult currentUserMenuTree(
			@ApiParam(name = "userId", value = "当前用户ID", required = true) @RequestParam(value = "userId", required = true) String userId,
			@ApiParam(name = "includeBtn", value = "是否包含按钮，传入1表示只包含菜单，传入2表示包含菜单和按钮", required = true) @RequestParam(value = "includeBtn", required = true) String includeBtn) {
		String currentUserId = ShiroContextUtil.currentUserId();
		if (!StringUtils.equalsIgnoreCase(currentUserId, userId)) {
			return SoulResult.warn("您无法获取别人的菜单");
		}
		if ("1".equals(includeBtn) || "2".equals(includeBtn)) {
			Map<String, Object> param = Maps.newHashMap();
			param.put("id", userId);
			param.put("includeBtn", includeBtn);
			MenuTree<BsMenuDO> menuTree = bsMenuService.findUserMenus(param);
			return SoulResult.success("获取用户菜单树成功", menuTree);
		} else {
			return SoulResult.warn("参数includeBtn不支持");
		}
		
	}
	
	@DS(Constants.AUTO_ROUTING)
	@RequiresPermissions("menu:view")
	@GetMapping("/menuTree")
	@ApiOperation(value = "查询菜单树" , httpMethod = "GET"  ,  notes = "根据参数获取菜单树" , produces = "application/json;charset=UTF-8")
	public SoulResult initMenuTree(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(name="menuName", value="菜单名称", required=false) @RequestParam(value="menuName",required=false) String menuName) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("menuName", menuName);
		MenuTree<BsMenuDO> menuTree = bsMenuService.initMenuTree(param);
		
		return SoulResult.success("初始化菜单树成功", menuTree);
	}
	
	@DS(Constants.AUTO_ROUTING)
	@RequiresPermissions("menu:add")
	@PostMapping(value = "/insert")
	@ControllerEndpoint(operation = "新增菜单" , exceptionMessage = "新增菜单失败")
	@ApiOperation(value = "新增菜单" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增菜单,非必输项为冗余字段")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response, BsMenuDO bsMenuDO) {
		try {
			if (bsMenuDO == null 
					|| StringUtils.isEmpty(bsMenuDO.getPid())
					|| StringUtils.isEmpty(bsMenuDO.getMenuName())
				  //|| StringUtils.isEmpty(bsMenuDO.getIcon())
					|| bsMenuDO.getType() == null) {
				return SoulResult.warn("菜单父类、菜单名称、按钮图标、按钮类型不能为空");
			}
			Map<String, Object> param = Maps.newHashMap();
			param.put("menuName", bsMenuDO.getMenuName());
			List<BsMenuDO> list = bsMenuService.select(param);
			if(list != null && list.size() > 0){
				return SoulResult.warn("菜单已存在，请修改后再提交");
			}
			//保存
			bsMenuDO.setCreateUser(ShiroContextUtil.currentUsername());
			int temp = bsMenuService.insert(bsMenuDO);
			if (temp > 0) {
				return SoulResult.success("新增成功");
			} else {
				return SoulResult.error("新增失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("新增异常,ERROR:" + e.getMessage());
		}
	}
	
	@DS(Constants.AUTO_ROUTING)
	@RequiresPermissions("menu:update")
	@PostMapping(value = "/update")
	@ControllerEndpoint(operation = "修改菜单" , exceptionMessage = "修改菜单失败")
	@ApiOperation(value = "修改菜单" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "修改菜单,非必输项为冗余字段")
	public SoulResult update(HttpServletRequest request, HttpServletResponse response, BsMenuDO bsMenuDO) {
		try {
			if (bsMenuDO == null 
					|| StringUtils.isEmpty(bsMenuDO.getId())
					|| StringUtils.isEmpty(bsMenuDO.getPid())
					|| StringUtils.isEmpty(bsMenuDO.getMenuName())
				  //|| StringUtils.isEmpty(bsMenuDO.getIcon())
					|| bsMenuDO.getType() == null) {
				return SoulResult.warn("ID、菜单父类、菜单名称、按钮图标、按钮类型不能为空");
			}
			Map<String, Object> param = Maps.newHashMap();
			param.put("menuName", bsMenuDO.getMenuName());
//			List<BsMenuDO> list = bsMenuService.select(param);
//			if(list != null && list.size() > 0){
//				return SoulResult.warn("菜单已存在，请修改后再提交");
//			}
			//保存
			bsMenuDO.setModifyUser(ShiroContextUtil.currentUsername());
			int temp = bsMenuService.modifty(bsMenuDO);
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
	@RequiresPermissions("menu:delete")
	@DeleteMapping("/deleteById/{id}")
	@ControllerEndpoint(operation = "删除菜单" , exceptionMessage = "删除菜单失败")
	@ApiOperation(value = "通过ID删除菜单", httpMethod="DELETE" , notes = "通过ID删除菜单" , produces = "application/json;charset=UTF-8")
	public SoulResult deleteMenu(
			@ApiParam(name="id",value="菜单ID",required=true) @RequestParam(value="id",required=true) String id){
		// 查询菜单是否存在
		BsMenuDO bsMenuDO = bsMenuService.findById(id);
		if (bsMenuDO != null) {
			Integer result = bsMenuService.physicalDelete(id);
			if (result ==  1) {
				return SoulResult.success("菜单删除成功");
			} else {
				return SoulResult.error("菜单删除失败");
			}
		} else {
			return SoulResult.warn("当前记录不存在");
		}
	}
	
	
	@DS(Constants.AUTO_ROUTING)
	@GetMapping("/selectById/{id}")
	@ApiOperation(value = "根据ID查询菜单", httpMethod="GET" , notes = "根据ID查询菜单" , produces = "application/json;charset=UTF-8")
	public SoulResult selectById(
			@ApiParam(name="id",value="菜单ID",required=true) @RequestParam(value="id",required=true) String id) {
		BsMenuDO menu = bsMenuService.findById(id);
		return SoulResult.success("查询成功", menu);
	}
	
}
