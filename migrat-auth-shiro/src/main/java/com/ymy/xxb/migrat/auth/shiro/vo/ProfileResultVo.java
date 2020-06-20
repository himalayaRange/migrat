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

package com.ymy.xxb.migrat.auth.shiro.vo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ymy.xxb.migrat.auth.shiro.constant.ShiroConst;
import com.ymy.xxb.migrat.auth.shiro.entity.BsRoleDO;
import com.ymy.xxb.migrat.auth.shiro.entity.BsUserDO;
import com.ymy.xxb.migrat.common.vo.ProfileResult;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义Authentication对象
 * 
 * @author wangyi
 *
 */
@Setter
@Getter
public class ProfileResultVo extends ProfileResult{
	
	private static final long serialVersionUID = 5196171566044413187L;

	/**
	 * user
	 * @param bsUserDO
	 */
	public ProfileResultVo(BsUserDO bsUserDO) {
		this.tenantId = bsUserDO.getTenantId();
		this.id = bsUserDO.getId();
		this.username = bsUserDO.getUsername();
		this.mobile = bsUserDO.getMobile();
		this.companyId = bsUserDO.getCompanyId();
		String bsRoles = bsUserDO.getBsRoles();
		Set<BsRoleDO> set = JSONObject.parseObject(bsRoles, new TypeReference<Set<BsRoleDO>>(){});
		Set<String> roles = new HashSet<>();
		Set<String> permissions = new HashSet<>();
		if (set != null && set.size() > 0 ) {
			for (BsRoleDO role : set) {
				String bsPermissions = role.getBsPermissions();
				Set<String> perms = JSONObject.parseObject(bsPermissions, new TypeReference<Set<String>>(){});
				for (String perm : perms) {
					permissions.add(perm);
				}
				roles.add(role.getRoleCode());
			}
		}
		this.roles.put(ShiroConst.PROFILE_ROLES, roles);
		this.roles.put(ShiroConst.PROFILE_PERMS, permissions);
	}
	
	/**
	 * coAdmin 、SaaSAdmin
	 * @param bsUserDO
	 * @param list
	 */
	public ProfileResultVo(BsUserDO bsUserDO , List<String> list) {
		this.id = bsUserDO.getId();
		this.username = bsUserDO.getUsername();
		this.mobile = bsUserDO.getMobile();
		this.companyId = bsUserDO.getCompanyId();
		Set<String> permissions = new HashSet<>();
		if (list != null && list.size() > 0 ) {
			for(String perm : list) {
				permissions.add(perm);
			}
		}
		this.roles.put("perms", permissions);
	}
	
}
