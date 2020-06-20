package com.ymy.xxb.migrat.auth.shiro.mapper;

import com.ymy.xxb.migrat.common.mapper.BaseMapper;

public interface BsUserRoleMapper extends BaseMapper{
	
	void removeUserRoleByUserId(String userId);
	
	void removeUserRoleByRoleId(String roleId);
}
