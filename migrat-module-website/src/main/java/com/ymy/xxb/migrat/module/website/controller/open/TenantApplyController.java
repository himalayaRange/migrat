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
import com.ymy.xxb.migrat.common.annotation.ControllerEndpoint;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.page.PageParamUtil;
import com.ymy.xxb.migrat.common.page.PageUtils;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.website.constant.ModuleConst;
import com.ymy.xxb.migrat.module.website.entity.TenantApplyDO;
import com.ymy.xxb.migrat.module.website.service.TenantApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 租户申请信息控制器
 * 
 * @author: wangyi
 *
 */
@RestController
@RequestMapping(Constants.API_OPEN_PREFIX +  ModuleConst.Access.API_MODULE_TENANT_APPLY)
@Api(value = "TenantApply API", tags = "TenantApply", description = "租户申请管理器")
public class TenantApplyController {

	@Autowired
	TenantApplyService tenantApplyService;

	
	/**
	 * 分页查询客户信息录入列表
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
	@ApiOperation(value = "获取租户申请信息列表" , httpMethod = "GET" , notes = "获取租户申请信息列表" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> param = PageParamUtil.pageParam(request);
			PageUtils<TenantApplyDO> page = tenantApplyService.selectPage(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}
	
	
	/**
	 * 新增租户申请信息
	 * @param request
	 * @param response
	 * @param tenantApplyDO
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@PostMapping(value = "/insert")
	@ApiOperation(value = "新增租户申请信息" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增租户申请信息")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response, TenantApplyDO tenantApplyDO) {
		try {
			if (tenantApplyDO == null
					|| StringUtils.isEmpty(tenantApplyDO.getCompanyName())
					|| StringUtils.isEmpty(tenantApplyDO.getMobile())){
				return SoulResult.warn("公司名称,手机号不能为空");
			}
			//校验类别编码,类别名称
			Map<String, Object> param = Maps.newHashMap();
			param.put("companyName", tenantApplyDO.getCompanyName());
			param.put("mobile", tenantApplyDO.getMobile());
			List<TenantApplyDO> list = tenantApplyService.select(param);
            if(list != null && list.size() > 0){
                return SoulResult.warn("公司名称,手机号已存在");
            }
			int temp = tenantApplyService.insert(tenantApplyDO);
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
	 * 商家审核 修改默认状态
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@PostMapping(value = "/update")
	@ControllerEndpoint(operation = "审核租户" , exceptionMessage = "审核租户失败")
	@ApiOperation(value = "审核租户" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "审核租户")
	public SoulResult update(HttpServletRequest request, HttpServletResponse response,TenantApplyDO tenantApplyDO) {
		try {

			int temp = tenantApplyService.update(tenantApplyDO);
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
	
}
