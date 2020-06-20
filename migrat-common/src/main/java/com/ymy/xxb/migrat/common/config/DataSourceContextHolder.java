package com.ymy.xxb.migrat.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ymy.xxb.migrat.common.constant.Constants;

public class DataSourceContextHolder {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceContextHolder.class);

	public static final String DEFAULT_DS = Constants.DEFAULT_DS;

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	
	// 启动标志变量，用于系统初始化
	private static final ThreadLocal<String> startedContextHolder = new ThreadLocal<String>();
	
	// 租户临时变量，用于系统登录未认证前租户路由的数据库存储
	private static final ThreadLocal<String> tenantContextHolder = new ThreadLocal<String>();
	
	public static void setDB(String dbType) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("switch to{ " + dbType + " } dataSource...");
		}
		contextHolder.set(dbType);
	}

	public static String getDB() {
		return contextHolder.get();
	}

	public static void clearDB() {
		contextHolder.remove();
	}
	
	
	public static void setTenantContext(String tenant) {
		
		tenantContextHolder.set(tenant);
	}

	public static String getTenantContext() {
		
		return tenantContextHolder.get();
	}

	public static void clearTenantContext() {
		
		tenantContextHolder.remove();
	}
	
	public static void setStartedContext(String started) {
		
		startedContextHolder.set(started);
	}

	public static String getStartedContext() {
		
		return startedContextHolder.get();
	}

	public static void clearStartedContext() {
		
		startedContextHolder.remove();
	}
}
