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
import com.ymy.xxb.migrat.module.biz.produce.entity.InformNodeDO;
import com.ymy.xxb.migrat.module.biz.produce.service.InformLabelTemplateService;
import com.ymy.xxb.migrat.module.biz.produce.service.InformNodeService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 通知节点模块
 *
 *
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.Api.API_MODULE_INFORM_NODE)
@Api(value = "InformNode API", tags = "InformNode", description = "通知节点控制器")
public class InformNodeController {

	@Autowired
	private InformNodeService informNodeService;

	@Autowired
	private InformLabelTemplateService informLabelTemplateService;
	/**
	 * 查询通知节点模块信息
	 * @param request
	 * @param response
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping(value = "/select")
	@ApiOperation(value = "获取通知节点模块信息列表" , httpMethod = "GET"  ,  notes = "根据参数获取通知节点模块列表" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {

		try {

			Map<String, Object> param = Maps.newHashMap();
			List<InformNodeDO> page = informNodeService.selectAll(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}


	/**
	 * 按通知节点ID逻辑删除通知节点信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@DeleteMapping("/delete")
	@ControllerEndpoint(operation = "删除通知节点" , exceptionMessage = "删除通知节点失败")
	@ApiOperation(value = "删除通知节点信息" ,httpMethod = "DELETE"  , notes = "按通知节点ID删除通知节点信息")
	public SoulResult delete(
			@ApiParam(name="id", value="通知节点ID",required=true)@RequestParam(value = "id", required = true) String id) {
		try {
			int temp = informNodeService.logicDelete(id);
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

	/**
	 * 按中间表ID删除消息节点模块信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@DeleteMapping("/deleteById")
	@ControllerEndpoint(operation = "删除中间表" , exceptionMessage = "删除中间表失败")
	@ApiOperation(value = "删除中间表信息" ,httpMethod = "DELETE"  , notes = "按ID删除中间表")
	public SoulResult deleteById(
			@ApiParam(name="id", value="中间表ID",required=true)@RequestParam(value = "id", required = true) String id) {
		try {
			int temp = informLabelTemplateService.deleteById(id);
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
