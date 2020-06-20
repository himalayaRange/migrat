package com.ymy.xxb.migrat.sso.server.config;

import com.ymy.xxb.migrat.sso.server.constant.Const;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSourceContextHolder {

	public static final String DEFAULT_DS = Const.SSO_MASTRE;

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setDB(String dbType) {
		if (log.isInfoEnabled()) {
			log.info("SWITCH TO { " + dbType + " } DATASOURCE...");
		}
		contextHolder.set(dbType);
	}

	public static String getDB() {
		return contextHolder.get();
	}

	public static void clearDB() {
		contextHolder.remove();
	}

}
