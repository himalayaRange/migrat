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
import com.ymy.xxb.migrat.auth.shiro.entity.DepartmentDO;
import com.ymy.xxb.migrat.auth.shiro.utils.ShiroContextUtil;
import com.ymy.xxb.migrat.common.annotation.ControllerEndpoint;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.page.PageParamUtil;
import com.ymy.xxb.migrat.common.page.PageUtils;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.biz.produce.constant.ModuleConst;
import com.ymy.xxb.migrat.module.biz.produce.entity.InformLabelTemplateDO;
import com.ymy.xxb.migrat.module.biz.produce.entity.MessageLabelDO;
import com.ymy.xxb.migrat.module.biz.produce.service.InformLabelTemplateService;
import com.ymy.xxb.migrat.module.biz.produce.service.MessageLabelService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 消息标签模块
 *
 *
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.Api.API_MODULE_MESSAGE_LABEL)
@Api(value = "MessageLabel API", tags = "MessageLabel", description = "消息标签控制器")
public class MessageLabelController {

	@Autowired
	private MessageLabelService messageLabelService;

    @Autowired
    private InformLabelTemplateService informLabelTemplateService;


	/**
	 * 分页查询消息标签模块信息
	 * @param request
	 * @param response
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@GetMapping(value = "/select")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "标签名称", name = "labelName", dataType = "String", paramType = "query"),
			@ApiImplicitParam(value = "当前页数", name = "page", dataType = "Integer", paramType = "query", required = true),
			@ApiImplicitParam(value = "每页显示条数", name = "rows", dataType = "Integer", paramType = "query", required = true)
	})
	@ApiOperation(value = "获取消息标签模块信息列表" , httpMethod = "GET"  ,  notes = "根据参数获取消息标签模块列表" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {

		try {
			Map<String, Object> param = PageParamUtil.pageParam(request);
			PageUtils<MessageLabelDO> page = messageLabelService.selectPage(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}

	/**
	 * 按ID查询消息标签信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@GetMapping("/findById/{id}")
	@ApiOperation(value = "按ID查询消息标签信息", httpMethod="GET" , notes = "按标签ID查询消息标签信息" , produces = "application/json;charset=UTF-8")
	public SoulResult findById(
			@ApiParam(name="id",value="标签ID",required=true) @RequestParam(value="id",required=true) String id) {
		if(id == null){

			return SoulResult.warn("缺少参数：ID");
		}
		try {
			MessageLabelDO messageLabelDO = messageLabelService.findById(id);

			return SoulResult.success("查询成功", messageLabelDO);
		} catch (Exception e) {

			return SoulResult.error(e.getMessage());
		}
	}

	/**
	 * 新增消息标签模块信息
	 * @param request
	 * @param response
	 * @param messageLabelDO
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@PostMapping(value = "/insert")
	@ControllerEndpoint(operation = "新增消息标签模块" , exceptionMessage = "新增消息标签模块失败")
	@ApiOperation(value = "新增消息标签模块" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增消息标签模块信息")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response, MessageLabelDO messageLabelDO) {
		try {
			if (messageLabelDO == null
					|| StringUtils.isEmpty(messageLabelDO.getApplyModule())
					|| StringUtils.isEmpty(messageLabelDO.getLabelName())) {
				return SoulResult.warn("适用模块,标签名称不能为空");
			}
			//校验适用模块,标签名称
			Map<String, Object> param = Maps.newHashMap();
			param.put("applyModule", messageLabelDO.getApplyModule());
			param.put("labelName", messageLabelDO.getLabelName());
			List<DepartmentDO> list = messageLabelService.select(param);
			if(list != null && list.size() > 0){
				return SoulResult.warn("适用模块,标签名称已存在");
			}
			//保存
			messageLabelDO.setCreateUser(ShiroContextUtil.currentUsername());
			int temp = messageLabelService.insert(messageLabelDO);
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
	 * 修改消息标签模块
	 * @param request
	 * @param response
	 * @param messageLabelDO
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@PostMapping(value = "/update")
	@ControllerEndpoint(operation = "修改消息标签模块" , exceptionMessage = "修改消息标签模块")
	@ApiOperation(value = "修改消息标签模块" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "修改消息通知模块信息")
	public SoulResult update(HttpServletRequest request, HttpServletResponse response, MessageLabelDO messageLabelDO) {
		try {
			if (messageLabelDO == null
					|| StringUtils.isEmpty(messageLabelDO.getApplyModule())
					|| StringUtils.isEmpty(messageLabelDO.getLabelName())) {
				return SoulResult.warn("适用模块,标签名称不能为空");
			}
			messageLabelDO.setModifyUser(ShiroContextUtil.currentUsername());
			int temp = messageLabelService.update(messageLabelDO);
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
	 * 按消息标签ID逻辑删除消息模块信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@DeleteMapping("/delete")
	@ControllerEndpoint(operation = "删除消息标签" , exceptionMessage = "删除消息标签失败")
	@ApiOperation(value = "删除消息标签信息" ,httpMethod = "DELETE"  , notes = "按消息标签ID删除消息标签信息")
	public SoulResult delete(
			@ApiParam(name="id", value="消息标签ID",required=true)@RequestParam(value = "id", required = true) String id) {
		try {
            InformLabelTemplateDO informLabelTemplateDO = informLabelTemplateService.findByLabelId(id);
            if (informLabelTemplateDO != null) {
                return SoulResult.success("该内容被使用,无法删除");
            }
            int temp = messageLabelService.logicDelete(id);
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
