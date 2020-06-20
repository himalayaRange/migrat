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
import com.ymy.xxb.migrat.common.page.PageParamUtil;
import com.ymy.xxb.migrat.common.page.PageUtils;
import com.ymy.xxb.migrat.module.biz.produce.entity.NotifyDO;
import com.ymy.xxb.migrat.module.biz.produce.service.NotifyService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.biz.produce.constant.ModuleConst;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 消息通知模块
 * 
 * @author: wangyi
 *
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.Api.API_MODULE_NOTIFY)
@Api(value = "Notify API", tags = "Notify", description = "消息通知控制器")
public class NotifyController {

	@Autowired
	private NotifyService notifyService;


	/**
	 * 分页查询消息通知模块信息
	 * @param request
	 * @param response
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping(value = "/select")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "用户ID", name = "userId", dataType = "String", paramType = "query"),
			@ApiImplicitParam(value = "当前页数", name = "page", dataType = "Integer", paramType = "query", required = true),
			@ApiImplicitParam(value = "每页显示条数", name = "rows", dataType = "Integer", paramType = "query", required = true)
	})
	@ApiOperation(value = "获取消息通知模块信息列表" , httpMethod = "GET"  ,  notes = "根据参数获取消息通知模块列表" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {

		try {
			Map<String, Object> param = PageParamUtil.pageParam(request);
			PageUtils<NotifyDO> page = notifyService.selectPage(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}

	/**
	 * 新增消息通知模块信息
	 * @param request
	 * @param response
	 * @param notifyDO
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@PostMapping(value = "/insert")
	@ControllerEndpoint(operation = "新增消息通知模块" , exceptionMessage = "新增消息通知模块失败")
	@ApiOperation(value = "新增消息通知模块" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增消息通知模块信息")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response, NotifyDO notifyDO) {
		try {
			if (notifyDO == null
					|| StringUtils.isEmpty(notifyDO.getUserId())
					|| StringUtils.isEmpty(notifyDO.getStageId())) {
				return SoulResult.warn("通知用户ID,生产阶段ID不能为空");
			}
			//校验用户ID,生产阶段ID
			Map<String, Object> param = Maps.newHashMap();
			param.put("userId", notifyDO.getUserId());
			param.put("stageId", notifyDO.getStageId());
			List<DepartmentDO> list = notifyService.select(param);
			if(list != null && list.size() > 0){
				return SoulResult.warn("通知用户ID,生产阶段ID已存在");
			}
			//保存
			notifyDO.setCreateUser(ShiroContextUtil.currentUsername());
			int temp = notifyService.insert(notifyDO);
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
	 * 修改消息通知模块
	 * @param request
	 * @param response
	 * @param notifyDO
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@PostMapping(value = "/update")
	@ControllerEndpoint(operation = "修改消息通知模块" , exceptionMessage = "修改消息通知模块")
	@ApiOperation(value = "修改消息通知模块" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "修改消息通知模块信息")
	public SoulResult update(HttpServletRequest request, HttpServletResponse response, NotifyDO notifyDO) {
		try {
			if (notifyDO == null
					|| StringUtils.isEmpty(notifyDO.getUserId())
					|| StringUtils.isEmpty(notifyDO.getStageId())) {
				return SoulResult.warn("通知用户ID,生产阶段ID不能为空");
			}
			notifyDO.setModifyUser(ShiroContextUtil.currentUsername());
			int temp = notifyService.update(notifyDO);
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
	 * 按消息模块ID逻辑删除消息模块信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@DeleteMapping("/delete")
	@ControllerEndpoint(operation = "删除消息模块" , exceptionMessage = "删除消息模块失败")
	@ApiOperation(value = "删除消息模块信息" ,httpMethod = "DELETE"  , notes = "按消息模块ID删除消息模块信息")
	public SoulResult delete(
			@ApiParam(name="id", value="消息模块ID",required=true)@RequestParam(value = "id", required = true) String id) {
		try {
			int temp = notifyService.logicDelete(id);
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
