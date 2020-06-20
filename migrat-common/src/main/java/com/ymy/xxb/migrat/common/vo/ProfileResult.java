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

package com.ymy.xxb.migrat.common.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.crazycake.shiro.AuthCachePrincipal;
import com.ymy.xxb.migrat.common.constant.Constants;

/**
 * 自定义Authentication对象基类
 * 
 * @author wangyi
 *
 */
@Setter
@Getter
public class ProfileResult implements Serializable ,AuthCachePrincipal{
	
	private static final long serialVersionUID = -6987615624667799395L;
	
	/*
	 * 租户ID，代表当前用户属于那个租户
	 */
	public String tenantId;
	
	/*
	 * ID，当前登录用户的ID
	 */
	public String id;
	
	/*
	 * 用户名 
	 */
	public String username;
	
	/*
	 * 用户手机号
	 */
	public String mobile;
	
	/*
	 * 所属公司
	 */
	public String companyId;
	
	/*
	 * 角色权限信息
	 */
	public Map<String, Object> roles = new HashMap<String, Object>();

	/*
	 * 授权缓存的Key，当前的缓存在redis上
	 */
	@Override
	public String getAuthCacheKey() {

		return Constants.CACHE_PERMISSION_KEY;
	}
	
}
