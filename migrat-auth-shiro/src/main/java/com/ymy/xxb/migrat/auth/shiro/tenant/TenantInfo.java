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
package com.ymy.xxb.migrat.auth.shiro.tenant;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import com.ymy.xxb.migrat.auth.shiro.vo.ProfileResultVo;
import com.ymy.xxb.migrat.common.config.DataSourceContextHolder;
import com.ymy.xxb.migrat.common.exception.TenantException;
import lombok.Data;

/**
 * 获取租户信息，本项目从Shiro中获取，用户可以依据项目需求自行封装
 * 
 * 整个项目中必须唯一
 * 
 * @author: wangyi
 *
 */
@Component
@Data
public class TenantInfo {
	
	// 租户ID在数据库中的字段
	private String tenantColumn = "tenant_id";
	
	// 租户ID在数据库中的类型
	private String dataType = "varchar(255)";
	
	/**
	 * 获取租户ID
	 * @return
	 * @throws TenantException
	 */
	public String getTenantId() throws TenantException{
		String startedContext = DataSourceContextHolder.getStartedContext();
		// 未登录状态
		if (startedContext != null) {
			return null;
		} else {
		//  登录后由shiro管理	
			ProfileResultVo profile = (ProfileResultVo) SecurityUtils.getSubject().getPrincipal();
			return profile == null ? null : profile.getTenantId();
		}
	}
}
