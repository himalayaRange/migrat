package com.ymy.xxb.migrat.common.config;

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
/**
 * 如果不引人jpa引入mybatis的话，需要进行如下配置
 * destroyMethod = "close",initMethod = "init"
 * 
 * @author wangyi
 *
 */
@Configuration
public class DataSourceConfigurer {
	
	@Bean(name = "migrat", destroyMethod = "close", initMethod = "init")
	@Qualifier("migrat")
	@ConfigurationProperties(prefix = "spring.datasource.migrat")
	public DataSource dataSourceWithDbMaster () {
		return new DruidDataSource();
	}
	
	
	@Bean(name = "migrat-biz-1", destroyMethod = "close", initMethod = "init")
	@Qualifier("migrat-biz-1")
	@ConfigurationProperties(prefix = "spring.datasource.migrat-biz-1")
	public DataSource dataSourceWithDbSlave1 () {
	 	return new DruidDataSource();
    }
	
	
	/**
	 * 多数据源支持
	 * @return
	 */
	@Primary
	@Bean(name = "dynamicDataSource") 
	public DataSource dynamicDataSource() {
		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		dynamicDataSource.setDefaultTargetDataSource(dataSourceWithDbMaster());
		ConcurrentHashMap<Object,Object> concurrentHashMap = new ConcurrentHashMap<>();
		concurrentHashMap.put("migrat", dataSourceWithDbMaster());
		concurrentHashMap.put("migrat-biz-1", dataSourceWithDbSlave1());
		dynamicDataSource.setTargetDataSources(concurrentHashMap);
		
		return dynamicDataSource;
	}
	
	
	/**
	 * 多数据源事务支持
	 * @return
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		
		return new DataSourceTransactionManager(dynamicDataSource());
	}
}
