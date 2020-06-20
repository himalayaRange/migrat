package com.ymy.xxb.migrat.auth.shiro.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import com.ymy.xxb.migrat.auth.shiro.constant.ShiroConst;
import com.ymy.xxb.migrat.auth.shiro.realm.MyShiroRealm;
import com.ymy.xxb.migrat.auth.shiro.vo.ProfileResultVo;
import com.ymy.xxb.migrat.common.utils.ShiroSecurityUtils;
/**
 *
 * @author: wangyi
 *
 */
public class ShiroContextUtil {
	
	// 获取Subject
	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}
	
	// 获取Session
	public static Session getSession() {
		return getSubject().getSession();
	}
	
	// 获取密码
	public static String createGeneratorPassword(String username , String password) {
		String salt = "(" + username + ")";
		int hashIterations = ShiroConst.SHIRO_HASHITERATIONS;
		String md5Pwd = ShiroSecurityUtils.md5Hash(password, salt, hashIterations);
		return md5Pwd;
	}
	
	// 获取租户编码
	public static String createSimpleTenantCode(String companyId, String tenantId) {
		String salt = new StringBuilder("[").append(companyId).append("]").toString();
		return (ShiroSecurityUtils.md5Hash(tenantId, salt, ShiroConst.SHIRO_HASHITERATIONS)).toUpperCase();
	}
	
	// 清空权限缓存，项目采用redis缓存即情况redis下权限缓存
	public static void clearCachedAuth() {
		RealmSecurityManager rsm = (RealmSecurityManager)SecurityUtils.getSecurityManager();
		MyShiroRealm shiroRealm = (MyShiroRealm)rsm.getRealms().iterator().next();
		Subject subject = SecurityUtils.getSubject();
		String realmName = subject.getPrincipals().getRealmNames().iterator().next();
		SimplePrincipalCollection principals  = new SimplePrincipalCollection(subject.getPrincipal() , realmName);
		subject.runAs(principals );
		// 用realm删除principal
		Cache<Object, AuthorizationInfo> authorizationCache = shiroRealm.getAuthorizationCache();
		if (authorizationCache != null && principals != null) {
			authorizationCache.remove(subject.getPrincipals());
		}
		// 切换身份也刷新
		subject.releaseRunAs();
	}
	
	// 获取当前用户的登录名
	public static String currentUsername() {
	    ProfileResultVo profile = (ProfileResultVo) SecurityUtils.getSubject().getPrincipal();
	    return profile == null ? null : profile.getUsername();
	}
	
	// 获取当前用户的手机号
	public static String currentMobile() {
	    ProfileResultVo profile = (ProfileResultVo) SecurityUtils.getSubject().getPrincipal();
	    return profile == null ? null : profile.getMobile();
	}
	
	// 获取当前用户信息
	public static ProfileResultVo currentProfile() {
		return (ProfileResultVo) SecurityUtils.getSubject().getPrincipal();
	}
	
	// 获取当前登录用户ID
	public static String currentUserId() {
		ProfileResultVo profile = (ProfileResultVo) SecurityUtils.getSubject().getPrincipal();
		return profile == null ? null : profile.getId();
	}
	
	public static String currentUserCompanyId() {
		ProfileResultVo profile = (ProfileResultVo) SecurityUtils.getSubject().getPrincipal();
		return profile == null ? null : profile.getCompanyId();
	}
	
	// 获取当前租户ID
	public static String currentTenantId() {
		ProfileResultVo profile = (ProfileResultVo) SecurityUtils.getSubject().getPrincipal();
		return profile == null ? null : profile.getTenantId();
	}
	
	// 登出
	public static void logout() {
		getSubject().logout();
	}
	
}
