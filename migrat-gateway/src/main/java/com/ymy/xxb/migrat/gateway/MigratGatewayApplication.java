package com.ymy.xxb.migrat.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * Migrat project gateway
 *
 * @author: wangyi
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.ymy.xxb.migrat"})
public class MigratGatewayApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MigratGatewayApplication.class, args);
	}
	
}
