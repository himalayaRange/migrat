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
package com.ymy.xxb.migrat.monitor.dashboard.runnner;

import java.net.InetAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author: wangyi
 *
 */
@Slf4j
@Component
public class MigratStartUpRunner implements ApplicationRunner{
	@Value("${server.port}")
    private String port;
   
	@Value("${server.servlet.path:}")
    private String contextPath;
    
	@Value("${spring.profiles.active}")
    private String active;
	
	@Autowired
	private ConfigurableApplicationContext context; 
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			// 初始化一些东西
		} catch (Exception e) {
			e.printStackTrace();
            log.error(" ____   __    _   _ ");
            log.error("| |_   / /\\  | | | |");
            log.error("|_|   /_/--\\ |_| |_|__");
            log.error("                        ");
            log.error("MIGRAT MONITOR DASHBOARD 启动失败，{}", e.getMessage());
            // 关闭 Migrat
            context.close();
		}
		if (context.isActive()) {
			 InetAddress address = InetAddress.getLocalHost();
	            String dashboard = String.format("http://%s:%s", address.getHostAddress(), port);
	            String loginUrl = "/swagger-ui.html";
	            String dashboardUrl = "/hystrix/monitor?stream=" + dashboard + "/actuator/hystrix.stream";
	            if (StringUtils.isNotBlank(contextPath))
	            	dashboard += contextPath;
	            if (StringUtils.isNotBlank(loginUrl))
	            	dashboard += dashboardUrl;
	            log.info(" __    ___   _      ___   _     ____ _____  ____ ");
	            log.info("/ /`  / / \\ | |\\/| | |_) | |   | |_   | |  | |_  ");
	            log.info("\\_\\_, \\_\\_/ |_|  | |_|   |_|__ |_|__  |_|  |_|__ ");
	            log.info("                                                      ");

	            String os = System.getProperty("os.name");
                // 默认为 windows时才自动打开页面
                if (StringUtils.containsIgnoreCase(os, "windows")) {
                    //使用默认浏览器打开系统登录页
                    Runtime.getRuntime().exec("cmd  /c  start " + dashboard);
                } else {
                	// Mac OS 下打开默认浏览器
                	Runtime.getRuntime().exec("open " + dashboard);
                }
		}
	}

}
