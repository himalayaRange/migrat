package com.ymy.xxb.migrat.auth.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

/**
 *
 * 权限认证过滤器
 * @author: wangyi
 *
 */
public class PermissionFilter extends AccessControlFilter {
	
	/**
	 * 未授权跳转页面
	 */
	private String errorUrl;
	
	public String getErrorUrl() {
		return errorUrl;
	}

	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}
	
	 /**
     * 表示是否允许访问 ，如果允许访问返回true，否则false；
     * @param request
     * @param response
     * @param mappedValue 表示写在拦截器中括号里面的字符串 mappedValue 就是 [urls] 配置中拦截器参数部分
     * @return
     * @throws Exception
     */
	@Override
	protected boolean isAccessAllowed(ServletRequest request,	ServletResponse response, Object mappedValue) throws Exception {
		 Subject subject = getSubject(request, response);
		 String requestURL = getPathWithinApplication(request);
		 //URL进行鉴权
		 boolean permitted = subject.isPermitted(requestURL);
		 return permitted;
	}

	/**
     * onAccessDenied：表示当访问拒绝时是否已经处理了；如果返回 true 表示需要继续处理；如果返回 false 表示该拦截器实例已经处理了，将直接返回即可。
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	@Override
	protected boolean onAccessDenied(ServletRequest request,	ServletResponse response) throws Exception {
		HttpServletResponse hsp = (HttpServletResponse)response;
		HttpServletRequest  hrq = (HttpServletRequest)request;
		hsp.sendRedirect(hrq.getContextPath() + this.getErrorUrl());
		return false;
	}

}
