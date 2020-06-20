package com.ymy.xxb.migrat.common.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import com.ymy.xxb.migrat.common.constant.Constants;

public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceContextHolder.getDB() == null ?  Constants.DEFAULT_DS : DataSourceContextHolder.getDB();
	}

}
