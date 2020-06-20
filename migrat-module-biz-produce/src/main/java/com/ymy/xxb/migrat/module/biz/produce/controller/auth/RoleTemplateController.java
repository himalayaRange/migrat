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

import com.google.common.collect.Maps;
import com.ymy.xxb.migrat.auth.shiro.entity.BsRoleDO;
import com.ymy.xxb.migrat.common.annotation.ControllerEndpoint;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.biz.produce.constant.ModuleConst;
import com.ymy.xxb.migrat.module.biz.produce.entity.GroupRuleDO;
import com.ymy.xxb.migrat.module.biz.produce.entity.InformTemplateDO;
import com.ymy.xxb.migrat.module.biz.produce.service.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 角色模板绑定模块
 *
 *
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.Api.API_MODULE_ROLE_TEMPLE)
@Api(value = "RoleTemplate API", tags = "RoleTemplate", description = "角色模板控制器")
public class RoleTemplateController {

	@Autowired
	private InformTemplateService informTemplateService;

	@Autowired
	private GroupRuleService groupRuleService;

	@Autowired
	private MessageSendService messageSendService;

	@Autowired
	private RoleTemplateService roleTemplateService;

	@DS(Constants.AUTO_ROUTING)
	@GetMapping("/templeList")
	@ApiOperation(value = "获取模板下拉框" , httpMethod = "GET"  ,  notes = "获取模板下拉框" , produces = "application/json;charset=UTF-8")
	public SoulResult templeList(
			@ApiParam(name="keyword",value="模板名称关键字，支持模糊查询",required=false) @RequestParam(value="keyword",required=false) String keyword
	) {
		List<Map<String,Object>> templeList = informTemplateService.templeList(keyword);
		return SoulResult.success("获取模板信息成功", templeList);
	}

	@DS(Constants.AUTO_ROUTING)
	@GetMapping("/ruleList")
	@ApiOperation(value = "获取分组下拉框" , httpMethod = "GET"  ,  notes = "获取分组下拉框" , produces = "application/json;charset=UTF-8")
	public SoulResult ruleList(
			@ApiParam(name="keyword",value="规则名称关键字，支持模糊查询",required=false) @RequestParam(value="keyword",required=false) String keyword
	) {
		List<Map<String,Object>> ruleList = groupRuleService.ruleList(keyword);
		return SoulResult.success("获取分组信息成功", ruleList);
	}

	@DS(value = Constants.AUTO_ROUTING)
	@PostMapping(value = "/insert")
	@ApiOperation(value = "新增角色模板模块" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增通知模板模块信息")
	public SoulResult insert(@ApiParam(name = "roleName", value = "角色名称", required = true) @RequestParam(value = "roleName", required = true) String roleName,
							 @ApiParam(name = "ruleName", value = "分组规则名称", required = true) @RequestParam(value = "ruleName", required = true) String ruleName,
							 @ApiParam(name = "templateTitle", value = "消息模板名称", required = true) @RequestParam(value = "templateTitle", required = true) String templateTitle) {
		try {
			BsRoleDO bsRoleDO = messageSendService.selectByRoleName(roleName);
			if (bsRoleDO == null) {
				return SoulResult.success("角色名称不存在呦");
			}
			String bsRoleDOId = bsRoleDO.getId();
			GroupRuleDO groupRuleDO = messageSendService.selectByRuleName(ruleName);
			if (groupRuleDO == null) {
				return SoulResult.success("规则名称不存在呦");
			}
			String ruleDOId = groupRuleDO.getId();
			InformTemplateDO informTemplateDO = messageSendService.selectByTemplateTitle(templateTitle);
			if (informTemplateDO == null) {
				return SoulResult.success("消息模板不存在呦");
			}
			String templateDOId = informTemplateDO.getId();
			Integer temp = roleTemplateService.insertRoleTemple(bsRoleDOId,ruleDOId, templateDOId);
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

	/**
	 * 按角色模板ID删除信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@DeleteMapping("/delete")
	@ControllerEndpoint(operation = "删除角色模板" , exceptionMessage = "删除角色模板失败")
	@ApiOperation(value = "删除角色模板" ,httpMethod = "DELETE"  , notes = "按角色模板ID删除角色模板信息")
	public SoulResult delete(
			@ApiParam(name="id", value="角色模板ID",required=true)@RequestParam(value = "id", required = true) String id) {
		try {
			int temp = roleTemplateService.delete(id);
			if (temp > 0) {
				return SoulResult.success("删除成功");
			} else {
				return SoulResult.error("删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("删除异常,ERROR:" + e.getMessage());
		}
	}

	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping(value = "/select")
	@ApiOperation(value = "获取用户的消息模板" , httpMethod = "GET"  ,  notes = "根据参数获取获取用户的消息模板" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response,
			 @ApiParam(name = "username", value = "用户名", required = true) @RequestParam(value = "username", required = true) String username) {
		try {
			Map<String, Object> param = Maps.newHashMap();
			param.put("username", username);
			List<InformTemplateDO> page = roleTemplateService.select(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}
	
}
