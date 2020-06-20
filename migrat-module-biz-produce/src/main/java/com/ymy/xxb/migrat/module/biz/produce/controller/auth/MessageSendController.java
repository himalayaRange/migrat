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
import com.ymy.xxb.migrat.common.annotation.ControllerEndpoint;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.biz.produce.constant.ModuleConst;
import com.ymy.xxb.migrat.module.biz.produce.entity.GroupRuleDO;
import com.ymy.xxb.migrat.module.biz.produce.entity.InformTemplateDO;
import com.ymy.xxb.migrat.module.biz.produce.service.InformTemplateRuleService;
import com.ymy.xxb.migrat.module.biz.produce.service.MessageSendService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 消息发送管理模块
 *
 *
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.Api.API_MODULE_MESSAGE_SEND)
@Api(value = "MessageSend API", tags = "MessageSend", description = "消息发送控制器")
public class MessageSendController {

	@Autowired
	private MessageSendService messageSendService;

	@Autowired
	private InformTemplateRuleService informTemplateRuleService;


	/**
	 * 查询消息发送模块信息
	 * @param request
	 * @param response
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping(value = "/select")
	@ApiOperation(value = "获取消息发送模块信息列表", httpMethod = "GET", notes = "根据参数消息发送标签模块列表", produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {

		try {
			Map<String, Object> param = Maps.newHashMap();
			List<GroupRuleDO> list = messageSendService.getMessageSend(param);
			return SoulResult.success("查询成功", list);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}


	/**
	 * 添加分组规则名称回显
	 * @param request
	 * @param response
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping(value = "/selectGroupRule")
	@ApiOperation(value = "获取分组规则名称回显信息列表", httpMethod = "GET", notes = "获取分组规则名称回显列表", produces = "application/json;charset=UTF-8")
	public SoulResult selectGroupRule(HttpServletRequest request, HttpServletResponse response) {

		try {
			Map<String, Object> param = Maps.newHashMap();
			List<GroupRuleDO> list = messageSendService.selectGroupRule(param);
			return SoulResult.success("查询成功", list);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}

	/**
	 * 添加消息模板名称回显
	 * @param request
	 * @param response
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping(value = "/selectInformTemplate")
	@ApiOperation(value = "获取消息模板名称回显信息列表", httpMethod = "GET", notes = "获取消息模板名称回显列表", produces = "application/json;charset=UTF-8")
	public SoulResult selectInformTemplate(HttpServletRequest request, HttpServletResponse response) {

		try {
			Map<String, Object> param = Maps.newHashMap();
			List<InformTemplateDO> list = messageSendService.selectInformTemplate(param);
			return SoulResult.success("查询成功", list);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}

	/**
	 * 新增消息发送信息
	 *
	 * @param request
	 * @param response
	 * @param ruleName
	 * @param templateTitle
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@PostMapping(value = "/insert")
	@ControllerEndpoint(operation = "新增消息发送模块", exceptionMessage = "新增消息发送模块失败")
	@ApiOperation(value = "新增消息发送模块", httpMethod = "POST", produces = "application/json;charset=UTF-8", notes = "新增消息发送模块信息")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response,
							 @ApiParam(name = "ruleName", value = "分组规则名称", required = true) @RequestParam(value = "ruleName", required = true) String ruleName,
							 @ApiParam(name = "templateTitle", value = "消息模板名称", required = true) @RequestParam(value = "templateTitle", required = true) String templateTitle) {
		try {
			GroupRuleDO groupRuleDO = messageSendService.selectByRuleName(ruleName);
			String ruleDOId = groupRuleDO.getId();
			InformTemplateDO informTemplateDO = messageSendService.selectByTemplateTitle(templateTitle);
			String templateDOId = informTemplateDO.getId();
			if (templateDOId == null || ruleDOId == null) {
				return SoulResult.warn("规则名称，模板名称不能为空");
			}
			Integer temp = informTemplateRuleService.insertMessageSend(ruleDOId, templateDOId);
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
	 * 按消息发送ID逻辑删除消息发送模块信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@DeleteMapping("/delete")
	@ControllerEndpoint(operation = "删除消息发送标签" , exceptionMessage = "删除消息发送标签失败")
	@ApiOperation(value = "删除消息发送信息" ,httpMethod = "DELETE"  , notes = "按消息发送ID删除消息发送信息")
	public SoulResult delete(
			@ApiParam(name="id", value="消息发送ID",required=true)@RequestParam(value = "id", required = true) String id) {
		try {
			int temp = informTemplateRuleService.delete(id);
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

}
