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
import com.ymy.xxb.migrat.auth.shiro.utils.ShiroContextUtil;
import com.ymy.xxb.migrat.common.annotation.ControllerEndpoint;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.page.PageParamUtil;
import com.ymy.xxb.migrat.common.page.PageUtils;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.biz.produce.constant.ModuleConst;
import com.ymy.xxb.migrat.module.biz.produce.entity.InformLabelTemplateDO;
import com.ymy.xxb.migrat.module.biz.produce.entity.InformTemplateDO;
import com.ymy.xxb.migrat.module.biz.produce.entity.InformTemplateRuleDO;
import com.ymy.xxb.migrat.module.biz.produce.service.InformLabelTemplateService;
import com.ymy.xxb.migrat.module.biz.produce.service.InformTemplateRuleService;
import com.ymy.xxb.migrat.module.biz.produce.service.InformTemplateService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 通知模板模块
 *
 *
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.Api.API_MODULE_INFORM_TEMPLATE)
@Api(value = "InformTemplate API", tags = "InformTemplate", description = "通知模板控制器")
public class InformTemplateController {

	@Autowired
	private InformTemplateService informTemplateService;

    @Autowired
    private InformLabelTemplateService informLabelTemplateService;

    @Autowired
    private InformTemplateRuleService informTemplateRuleService;
	/**
	 * 分页查询通知模板模块信息
	 * @param request
	 * @param response
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping(value = "/select")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "模板标题", name = "templateTitle", dataType = "String", paramType = "query"),
			@ApiImplicitParam(value = "模板编号", name = "templateCode", dataType = "String", paramType = "query"),
			@ApiImplicitParam(value = "当前页数", name = "page", dataType = "Integer", paramType = "query", required = true),
			@ApiImplicitParam(value = "每页显示条数", name = "rows", dataType = "Integer", paramType = "query", required = true)
	})
	@ApiOperation(value = "获取通知模板模块信息列表" , httpMethod = "GET"  ,  notes = "根据参数获取通知模板模块列表" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {

		try {
			Map<String, Object> param = PageParamUtil.pageParam(request);
			PageUtils<InformTemplateDO> page = informTemplateService.selectPage(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}

	/**
	 * 按ID查询消息模板信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping("/findById/{id}")
	@ApiOperation(value = "按ID查询消息模板信息", httpMethod="GET" , notes = "按消息模板ID查询消息模板信息" , produces = "application/json;charset=UTF-8")
	public SoulResult findById(
			@ApiParam(name="id",value="消息模板ID",required=true) @RequestParam(value="id",required=true) String id) {
		if(id == null){

			return SoulResult.warn("缺少参数：ID");
		}
		try {
			InformTemplateDO informTemplateDO = informTemplateService.findById(id);

			return SoulResult.success("查询成功", informTemplateDO);
		} catch (Exception e) {

			return SoulResult.error(e.getMessage());
		}
	}

	/**
	 * 新增通知模板模块信息
	 * @param request
	 * @param response
	 * @param informTemplateDO
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@PostMapping(value = "/insert")
	@ControllerEndpoint(operation = "新增通知模板模块" , exceptionMessage = "新增通知模板模块失败")
	@ApiOperation(value = "新增通知模板模块" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增通知模板模块信息")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response, InformTemplateDO informTemplateDO) {
		try {
			if (informTemplateDO == null
					|| StringUtils.isEmpty(informTemplateDO.getSendNode())
					|| StringUtils.isEmpty(informTemplateDO.getTemplateTitle())
					|| StringUtils.isEmpty(informTemplateDO.getTemplateCode())) {
				return SoulResult.warn("发送节点,模板标题,模板编号不能为空");
			}
			//校验发送节点,模板标题,模板编号
			Map<String, Object> param = Maps.newHashMap();
			param.put("sendNode", informTemplateDO.getSendNode());
			param.put("templateTitle", informTemplateDO.getTemplateTitle());
			param.put("templateCode", informTemplateDO.getTemplateCode());
			List<InformTemplateDO> list = informTemplateService.select(param);
			if(list != null && list.size() > 0){
				return SoulResult.warn("发送节点,模板标题,模板编号已存在");
			}
			//保存
			informTemplateDO.setCreateUser(ShiroContextUtil.currentUsername());
			int temp = informTemplateService.insert(informTemplateDO);
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
	 * 修改通知模板模块
	 * @param request
	 * @param response
	 * @param informTemplateDO
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@PostMapping(value = "/update")
	@ControllerEndpoint(operation = "修改通知模板模块" , exceptionMessage = "修改通知模板模块")
	@ApiOperation(value = "修改通知模板模块" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "修改通知模板模块信息")
	public SoulResult update(HttpServletRequest request, HttpServletResponse response, InformTemplateDO informTemplateDO) {
		try {
			if (informTemplateDO == null
					|| StringUtils.isEmpty(informTemplateDO.getSendNode())
					|| StringUtils.isEmpty(informTemplateDO.getTemplateTitle())
					|| StringUtils.isEmpty(informTemplateDO.getTemplateCode())) {
				return SoulResult.warn("发送节点,模板标题,模板编号不能为空");
			}
			informTemplateDO.setModifyUser(ShiroContextUtil.currentUsername());
			int temp = informTemplateService.update(informTemplateDO);
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


	/**
	 * 按通知模板ID逻辑删除通知模板信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@DeleteMapping("/delete")
	@ControllerEndpoint(operation = "删除通知模板" , exceptionMessage = "删除通知模板失败")
	@ApiOperation(value = "删除通知模板信息" ,httpMethod = "DELETE"  , notes = "按通知模板ID删除通知模板信息")
	public SoulResult delete(
			@ApiParam(name="id", value="通知模板ID",required=true)@RequestParam(value = "id", required = true) String id) {
		try {
            InformLabelTemplateDO informLabelTemplateDO = informLabelTemplateService.findByTempId(id);
            InformTemplateRuleDO informTemplateRuleDO = informTemplateRuleService.findByTempleId(id);
            if (informLabelTemplateDO != null || informTemplateRuleDO != null) {
                return SoulResult.success("该内容被使用,无法删除");
            }
			int temp = informTemplateService.logicDelete(id);
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
