package com.ymy.xxb.migrat.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.ymy.xxb.migrat.job.constant.Const;

@EnableDiscoveryClient
@EnableAspectJAutoProxy
@ComponentScan(Const.APPLICATION_COMPONENT_SCAN)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
@EnableFeignClients(basePackages = com.ymy.xxb.migrat.job.constant.Const.FEIGN_CLIENTS_INFACE_SCAN)
public class XxlJobApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(XxlJobApplication.class, args);
	}
	
}
