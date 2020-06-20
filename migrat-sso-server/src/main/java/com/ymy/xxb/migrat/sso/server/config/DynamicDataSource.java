package com.ymy.xxb.migrat.sso.server.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.ymy.xxb.migrat.sso.server.constant.Const;

public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceContextHolder.getDB() == null ? Const.SSO_MASTRE : DataSourceContextHolder.getDB();
	}

}
