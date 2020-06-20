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
package com.ymy.xxb.migrat.monitor.dashboard.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

/*
 * 
 * Hystrix Dashboard
 *
 * @author: wangyi
 *
 */
@Configuration
public class HystrixDashboardConfig {

	@Bean(name = "hystrixRegistrationBean")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ServletRegistrationBean servletRegistrationBean() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new HystrixMetricsStreamServlet(),
				"/hystrix.stream");
		registration.setName("hystrixServlet");
		registration.setLoadOnStartup(1);
		return registration;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean(name = "hystrixForTurbineRegistrationBean")
	public ServletRegistrationBean servletTurbineRegistrationBean() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new HystrixMetricsStreamServlet(),
				"/actuator/hystrix.stream");
		registration.setName("hystrixForTurbineServlet");
		registration.setLoadOnStartup(1);
		return registration;
	}

}
