package com.ymy.xxb.migrat.auth.shiro.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;

public class UrlPermission implements Permission {

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public UrlPermission() {
		super();
	}

	public UrlPermission(String url) {
		super();
		this.url = url;
	}

	// 对URL进行自定义匹配
	@Override
	public boolean implies(Permission p) {
		if (!(p instanceof UrlPermission))
			return false;
		UrlPermission up = (UrlPermission) p;
		PatternMatcher patternMatcher = new AntPathMatcher();
		return patternMatcher.matches(this.getUrl(), up.getUrl());
	}

}
