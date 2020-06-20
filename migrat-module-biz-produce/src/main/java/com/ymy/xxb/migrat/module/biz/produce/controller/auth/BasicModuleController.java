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
import com.ymy.xxb.migrat.module.biz.produce.entity.BasicModuleDO;
import com.ymy.xxb.migrat.module.biz.produce.service.BasicModuleService;
import com.ymy.xxb.migrat.module.biz.produce.vo.BasicModuleTree;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 基础模块配置
 * 
 * @author: wangyi
 *
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX +  ModuleConst.Api.API_MODULE_BASIC_MODULE)
@Api(value = "BASIC_MODULE API", tags = "BasicModule", description = "基础模块控制器")
public class BasicModuleController {


	@Autowired
	private BasicModuleService basicModuleService;

	/**
	 * 基础模块配置树
	 * @param request
	 * @param response
	 * @param moduleName
	 * @return
	 */
	@DS(Constants.DEFAULT_DS)
	@GetMapping("/basicModuleTree")
	@ApiOperation(value = "查询基础模块配置树" , httpMethod = "GET"  ,  notes = "根据参数获取基础模块配置树" , produces = "application/json;charset=UTF-8")
	public SoulResult initDepartmentTree(HttpServletRequest request, HttpServletResponse response,
										 @ApiParam(name="moduleName", value="基础模块名称", required=false) @RequestParam(value="moduleName",required=false) String moduleName) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("moduleName", moduleName);
		BasicModuleTree<BasicModuleDO> basicModuleTree = basicModuleService.initBasicModuleTree(param);

		return SoulResult.success("初始化基础模块配置树成功", basicModuleTree);
	}



	/**
	 * 分页查询基础模块信息
	 * @param request
	 * @param response
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@GetMapping(value = "/select")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "模块名称", name = "moduleName", dataType = "String", paramType = "query"),
			@ApiImplicitParam(value = "模块编码", name = "moduleCode", dataType = "String", paramType = "query"),
			@ApiImplicitParam(value = "当前页数", name = "page", dataType = "Integer", paramType = "query", required = true),
			@ApiImplicitParam(value = "每页显示条数", name = "rows", dataType = "Integer", paramType = "query", required = true)
	})
	@ApiOperation(value = "获取模块配置信息列表" , httpMethod = "GET"  ,  notes = "根据参数获取模块配置列表" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {

		try {
			Map<String, Object> param = PageParamUtil.pageParam(request);
			PageUtils<DepartmentDO> page = basicModuleService.selectPage(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}



	/**
	 * 新增模块信息
	 * @param request
	 * @param response
	 * @param basicModuleDO
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@PostMapping(value = "/insert")
	@ControllerEndpoint(operation = "新增模块" , exceptionMessage = "新增模块失败")
	@ApiOperation(value = "新增模块" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增模块配置信息")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response, BasicModuleDO basicModuleDO) {
		try {
			if (basicModuleDO == null
					|| StringUtils.isEmpty(basicModuleDO.getModuleName())
					|| StringUtils.isEmpty(basicModuleDO.getModuleCode())) {
				return SoulResult.warn("模块名称,模块编码不能为空");
			}
			//校验模块名称,模块编码
			Map<String, Object> param = Maps.newHashMap();
			param.put("moduleName", basicModuleDO.getModuleName());
			param.put("moduleCode", basicModuleDO.getModuleCode());
			List<DepartmentDO> list = basicModuleService.select(param);
			if(list != null && list.size() > 0){
				return SoulResult.warn("模块名称,模块编码已存在");
			}
			//保存
			basicModuleDO.setCreateUser(ShiroContextUtil.currentUsername());
			int temp = basicModuleService.insert(basicModuleDO);
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
	 * 修改基础模块
	 * @param request
	 * @param response
	 * @param basicModuleDO
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@PostMapping(value = "/update")
	@ControllerEndpoint(operation = "修改基础模块" , exceptionMessage = "修改基础模块")
	@ApiOperation(value = "修改基础模块" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "修改基础模块信息")
	public SoulResult update(HttpServletRequest request, HttpServletResponse response, BasicModuleDO basicModuleDO) {
		try {
			if (basicModuleDO == null
					|| StringUtils.isEmpty(basicModuleDO.getModuleName())
					|| StringUtils.isEmpty(basicModuleDO.getModuleCode())) {
				return SoulResult.warn("模块名称,模块编码不能为空");
			}
			basicModuleDO.setModifyUser(ShiroContextUtil.currentUsername());
			int temp = basicModuleService.update(basicModuleDO);
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
	 * 按模块ID逻辑删除模块信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@DeleteMapping("/delete")
	@ControllerEndpoint(operation = "删除基础模块" , exceptionMessage = "删除基础模块失败")
	@ApiOperation(value = "删除基础模块信息" ,httpMethod = "DELETE"  , notes = "按基础模块ID删除基础模块信息")
	public SoulResult delete(
			@ApiParam(name="id", value="基础模块ID",required=true)@RequestParam(value = "id", required = true) String id) {
		try {
			int temp = basicModuleService.logicDelete(id);
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
