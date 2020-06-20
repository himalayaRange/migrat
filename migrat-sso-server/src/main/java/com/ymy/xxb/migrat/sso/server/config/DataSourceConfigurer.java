package com.ymy.xxb.migrat.sso.server.config;

import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import com.alibaba.druid.pool.DruidDataSource;
import com.ymy.xxb.migrat.sso.server.constant.Const;

@Configuration
public class DataSourceConfigurer {
	
	@Bean(name = Const.SSO_MASTRE)
	@Qualifier(Const.SSO_MASTRE)
	@ConfigurationProperties(prefix = "spring.datasource." + Const.SSO_MASTRE)
	public DataSource dataSourceWithDbMaster () {

		return new DruidDataSource();
	}
	
	
	@Primary
	@Bean(name = "dynamicDataSource") 
	public DataSource dynamicDataSource() {
		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		// 默认数据源
		dynamicDataSource.setDefaultTargetDataSource(dataSourceWithDbMaster());
		// 配置多数据源
		ConcurrentHashMap<Object,Object> concurrentHashMap = new ConcurrentHashMap<>();
		concurrentHashMap.put(Const.SSO_MASTRE, dataSourceWithDbMaster());
		dynamicDataSource.setTargetDataSources(concurrentHashMap);
		
		return dynamicDataSource;
	}
	
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		
		return new DataSourceTransactionManager(dynamicDataSource());
	}
}
