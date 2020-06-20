package com.ymy.xxb.migrat.module.comyany;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.ymy.xxb.migrat.common.utils.IdWorker;
import com.ymy.xxb.migrat.module.comyany.constant.Const;

@EnableAsync
@EnableScheduling
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan(Const.APPLICATION_COMPONENT_SCAN)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan(basePackages = {"com.ymy.xxb.migrat.module.comyany.mapper","com.ymy.xxb.migrat.auth.shiro.mapper"})
@EnableFeignClients(basePackages = com.ymy.xxb.migrat.coodinator.register.constant.Const.FEIGN_CLIENTS_INFACE_SCAN)
public class MigratModuleCompanyApplication {
	
	
	public static void main(final String[] args) {
		SpringApplication.run(MigratModuleCompanyApplication.class, args);
	}
	
	@Bean
	public IdWorker idWorker() {
		return new IdWorker();
	}
	
}
