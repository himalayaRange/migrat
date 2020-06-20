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
package com.ymy.xxb.migrat.monitor.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import lombok.extern.slf4j.Slf4j;

/**
 * Monitor Dashboard
 *
 * @author: wangyi
 *
 */
@Slf4j
@EnableTurbine
@EnableDiscoveryClient
@SpringBootApplication
@EnableHystrixDashboard  
public class MigratMonitorDashboardApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MigratMonitorDashboardApplication.class, args);
		log.info(">>>>>>> Monitor Dashboard Startup>>>>>");
	}
}
