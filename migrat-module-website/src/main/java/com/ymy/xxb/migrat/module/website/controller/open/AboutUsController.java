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
package com.ymy.xxb.migrat.module.website.controller.open;

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Maps;
import com.ymy.xxb.migrat.auth.shiro.utils.ShiroContextUtil;
import com.ymy.xxb.migrat.common.annotation.ControllerEndpoint;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.page.PageParamUtil;
import com.ymy.xxb.migrat.common.page.PageUtils;
import com.ymy.xxb.migrat.module.website.entity.PressCenterDO;
import com.ymy.xxb.migrat.module.website.service.AboutUsService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.website.constant.ModuleConst;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 关于我们控制器
 * 
 * @author: wangyi
 *
 */
@RestController
@RequestMapping(Constants.API_OPEN_PREFIX +  ModuleConst.Access.API_MODULE_ABOUTUS)
@Api(value = "AboutUs API", tags = "AboutUs", description = "关于我们管理器")
public class AboutUsController {

	@Autowired
	AboutUsService aboutUsService;

	/**
	 * 新闻列表
	 * @param request
	 * @param response
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@GetMapping(value = "/select")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "当前页数", name = "page", dataType = "Integer", paramType = "query", required = true),
			@ApiImplicitParam(value = "每页显示条数", name = "rows", dataType = "Integer", paramType = "query", required = true)
	})
	@ApiOperation(value = "获取新闻列表" , httpMethod = "GET" , notes = "新区新闻列表信息" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> param = PageParamUtil.pageParam(request);
			PageUtils<PressCenterDO> page = aboutUsService.selectPage(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}
	/**
	 * 新增新闻
	 * @param request
	 * @param response
	 * @param pressCenterDO
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@PostMapping(value = "/insert")
	@ApiOperation(value = "新增新闻" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增新闻内容")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response, PressCenterDO pressCenterDO) {
		try {
			if (pressCenterDO == null
					|| StringUtils.isEmpty(pressCenterDO.getTitle())
					|| StringUtils.isEmpty(pressCenterDO.getContent())){
				return SoulResult.warn("新闻标题,新闻内容不能为空");
			}
			//校验新闻标题,新闻内容
			Map<String, Object> param = Maps.newHashMap();
			param.put("title", pressCenterDO.getTitle());
			param.put("content", pressCenterDO.getContent());
			List<PressCenterDO> list = aboutUsService.select(param);
			if(list != null && list.size() > 0){
				return SoulResult.warn("新闻标题,新闻内容已存在");
			}
			int temp = aboutUsService.insert(pressCenterDO);
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
	 * 修改新闻信息
	 * @param request
	 * @param response
	 * @param pressCenterDO
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@PostMapping(value = "/update")
	@ControllerEndpoint(operation = "修改新闻信息" , exceptionMessage = "修改新闻信息失败")
	@ApiOperation(value = "修改新闻信息" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "修改新闻信息")
	public SoulResult update(HttpServletRequest request, HttpServletResponse response, PressCenterDO pressCenterDO) {
		try {
			if (pressCenterDO == null
					|| StringUtils.isEmpty(pressCenterDO.getTitle())
					|| StringUtils.isEmpty(pressCenterDO.getContent())){
				return SoulResult.warn("新闻标题,新闻内容不能为空");
			}
			pressCenterDO.setModifyUser(ShiroContextUtil.currentUsername());
			int temp = aboutUsService.update(pressCenterDO);
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
	 * 按新闻ID逻辑删除新闻信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@DeleteMapping("/delete")
	@ControllerEndpoint(operation = "删除新闻" , exceptionMessage = "删除新闻失败")
	@ApiOperation(value = "删除新闻信息" ,httpMethod = "DELETE"  , notes = "按新闻ID删除新闻信息")
	public SoulResult delete(
			@ApiParam(name="id", value="新闻ID",required=true)@RequestParam(value = "id", required = true) String id) {
		try {
			int temp = aboutUsService.logicDelete(id);
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
