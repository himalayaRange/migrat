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
package com.ymy.xxb.migrat.auth.shiro.runner;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.google.common.collect.Maps;
import com.ymy.xxb.migrat.auth.shiro.properties.MigratProperties;
import com.ymy.xxb.migrat.auth.shiro.service.BsUserService;
import com.ymy.xxb.migrat.auth.shiro.service.CompanyService;
import com.ymy.xxb.migrat.common.config.DataSourceContextHolder;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.utils.DbContextUtil;
import lombok.extern.slf4j.Slf4j;
/**
 *
 * @author: wangyi
 *
 */
@Slf4j
@Component
public class MigratStartUpRunner implements ApplicationRunner{
	@Autowired
	private ConfigurableApplicationContext context;
	
	@Autowired
	private MigratProperties migratProperties;
	
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private BsUserService bsUserService;
	 
	@Value("${server.port}")
    private String port;
   
	@Value("${server.servlet.path:}")
    private String contextPath;
    
	@Value("${spring.profiles.active}")
    private String active;
    
	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
            // 做相关必要的检查和资源初始化
			// initUserRouteInfo();
        } catch (Exception e) {
        	e.printStackTrace();
            log.error(" ____   __    _   _ ");
            log.error("| |_   / /\\  | | | |");
            log.error("|_|   /_/--\\ |_| |_|__");
            log.error("                        ");
            log.error("MIGRAT启动失败，{}", e.getMessage());
            // 关闭 Migrat
            context.close();
        }
        if (context.isActive()) {
            InetAddress address = InetAddress.getLocalHost();
            String url = String.format("http://%s:%s", address.getHostAddress(), port);
            String loginUrl = "/";
            if (StringUtils.isNotBlank(contextPath))
                url += contextPath;
            if (StringUtils.isNotBlank(loginUrl))
                url += loginUrl;
            log.info(" __    ___   _      ___   _     ____ _____  ____ ");
            log.info("/ /`  / / \\ | |\\/| | |_) | |   | |_   | |  | |_  ");
            log.info("\\_\\_, \\_\\_/ |_|  | |_|   |_|__ |_|__  |_|  |_|__ ");
            log.info("                                                      ");
            log.info("MIGRAT 权限系统启动完毕，地址：{}", url);

            boolean auto = migratProperties.isAutoOpenBrowser();
            // String[] autoEnv = migratProperties.getAutoOpenBrowserEnv();
            if (auto) {
                String os = System.getProperty("os.name");
                // 默认为 windows时才自动打开页面
                if (StringUtils.containsIgnoreCase(os, "windows")) {
                    //使用默认浏览器打开系统登录页
                    Runtime.getRuntime().exec("cmd  /c  start " + url);
                } else {
                	// Mac OS 下打开默认浏览器
                	Runtime.getRuntime().exec("open " + url);
                }
            }
        }
	}
	
	/**
	 * 初始化租户gua路由信息
	 * 一个库10各租户，一个租户默认200人使用，则 键值对数量 2000
	 * 实现进阶：
	 * 	1.redis中存储 + mysql（备用）
	 *   
	 *  2.zookeeper快速索引
	 * @return
	 */
	@Async(value = Constants.ASYNC_POOL)
	private void initUserRouteInfo() {
		Integer tenantIdGroupUnit = Constants.TENANT_ID_GROUP_UNIT;
		// 设置启动标志，此时shiro还未被管理起来
		DataSourceContextHolder.setStartedContext(Constants.STARTED_FLAG);
		String maxTenantId = companyService.maxTenantId();
		Integer max = Integer.valueOf(maxTenantId);
		// 判断当前系统中业务子系统一共多少个并循环
		Integer dbCount = (max / tenantIdGroupUnit) + 1;
		for (int i = 1 ; i <= dbCount ; i ++) {
			// 更改数据源
			DataSourceContextHolder.setTenantContext(DbContextUtil.getBizDbName(i));
			List<Map<String,Object>> list = bsUserService.selectSimpleUsers();
			Map<String, Object> routMap = Maps.newHashMap();
			for (Map<String, Object> user : list) {
				routMap.put(user.get("mobile").toString(), user.get("tenantId"));
			}
			redisTemplate.opsForHash().putAll(Constants.CACHE_USERS_TENANT_ROUTING + "_" + String.valueOf(i), routMap);
			redisTemplate.persist(Constants.CACHE_USERS_TENANT_ROUTING + "_" + String.valueOf(i));
		}
		DataSourceContextHolder.clearTenantContext();
		DataSourceContextHolder.clearStartedContext();
	}
}
