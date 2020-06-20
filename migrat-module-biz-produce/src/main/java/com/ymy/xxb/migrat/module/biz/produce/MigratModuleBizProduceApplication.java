/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.ymy.xxb.migrat.module.biz.produce;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.ymy.xxb.migrat.common.utils.IdWorker;
import com.ymy.xxb.migrat.module.biz.produce.constant.Const;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author: wangyi
 *
 */
@EnableAsync
@EnableScheduling
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan(Const.APPLICATION_COMPONENT_SCAN)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan({"com.ymy.xxb.migrat.module.biz.produce.mapper","com.ymy.xxb.migrat.auth.shiro.mapper"})
@EnableFeignClients(basePackages = com.ymy.xxb.migrat.coodinator.register.constant.Const.FEIGN_CLIENTS_INFACE_SCAN)
public class MigratModuleBizProduceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MigratModuleBizProduceApplication.class, args);
	}
	
	@Bean
	public IdWorker idWorker() {
		return new IdWorker();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
}
