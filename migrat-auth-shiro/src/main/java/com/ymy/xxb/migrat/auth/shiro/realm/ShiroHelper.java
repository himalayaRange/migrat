package com.ymy.xxb.migrat.auth.shiro.realm;

import org.apache.shiro.authz.AuthorizationInfo;

import com.ymy.xxb.migrat.common.annotation.Helper;

@Helper
public class ShiroHelper extends MyShiroRealm{
	
	/**
	 * 获取当前用户角色和权限集合
	 * @return
	 */
	 public AuthorizationInfo getCurrentuserAuthorizationInfo() {
	        return super.doGetAuthorizationInfo(null);
	 }
}
