package com.ymy.xxb.migrat.configuration.constant;

/**
 * 
 *  Configoration Const
 * 
 * @author wangyi
 *
 */
public class Const {
	/*
	 * shiro sessionId key
	 */
	public static final String HEAFER_SESSION_ID = "Authorization";
	
	
	/*
	 * Tenanat 开放接口扫描包
	 */
	public static final String SCAN_TENANT_CONTR_OPEN_PACKAGE = "com.ymy.xxb.migrat.auth.shiro.controller";
	
	/*
	 * Tenant 授权接口扫描包
	 */
	public static final String SCAN_TENANT_CONTR_AUTH_PACKAGE = "com.ymy.xxb.migrat.module.comyany.controller";
	
	/* 
	 * Tenant Swagger访问地址
	 */
	public static final String SWAGGER2_TENANT_SERVICE_URL = "http://127.0.0.1:11000/swagger-ui.html";
	
	
	/*
	 * Website Swagger访问地址
	 */
	public static final String SWAGGER2_WEBSITE_SERVICE_URL = "http://127.0.0.1:8002/swagger-ui.html";
	
	/*
	 * Website开放接口扫描包
	 */
	public static final String SCAN_WEBSITE_CONTR_OPEN_PACKAGE = "com.ymy.xxb.migrat.module.website.controller.open";
	
	/*
	 * Website授权接口扫描包
	 */
	public static final String SCAN_WEBSITE_CONTR_AUTH_PACKAGE = "com.ymy.xxb.migrat.module.website.controller.auth";
	
	
	/*
	 * Biz Produce Swagger访问地址
	 */
	public static final String SWAGGER2_BIZ_PRODUCE_SERVICE_URL = "http://127.0.0.1:10002/swagger-ui.html";
	
	/*
	 * Biz Produce开放接口扫描包
	 */
	public static final String SCAN_BIZ_PRODUCE_CONTR_OPEN_PACKAGE = "com.ymy.xxb.migrat.module.biz.produce.controller.open";
	
	/*
	 * Biz Produce授权接口扫描包
	 */
	public static final String SCAN_BIZ_PRODUCE_CONTR_AUTH_PACKAGE = "com.ymy.xxb.migrat.module.biz.produce.controller.auth";
}
