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
package com.ymy.xxb.migrat.configuration.api.support;

import java.util.Properties;
import java.util.concurrent.Executor;
import org.springframework.stereotype.Service;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.ymy.xxb.migrat.configuration.api.MigratNacosConfig;
import com.ymy.xxb.migrat.configuration.properties.NacosProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author: wangyi
 *
 */
@Slf4j
@Service
public class MigratNacosConfigSupport implements MigratNacosConfig {
	
	
	@Override
	public String getConfig(NacosProperties nacosProperties) throws Exception {
		try {
			String dataId = nacosProperties.getDataId();
			String group = nacosProperties.getGroup();
			Long timeout = nacosProperties.getTimeout();
			Properties properties = new Properties();
			properties.put("namespace", nacosProperties.getNamespace());
			properties.put("serverAddr", nacosProperties.getServerAddr());
			ConfigService configService = NacosFactory.createConfigService(properties);
			String content = configService.getConfig(dataId, group, timeout);
			return content;
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Get Config From Nacos Error , [" + e.getMessage() + "]");
			}
			return null;
		}
	}

	@Override
	public void addListener(NacosProperties nacosProperties) throws Exception {
		try {
			String dataId = nacosProperties.getDataId();
			String group = nacosProperties.getGroup();
			Properties properties = new Properties();
			properties.put("namespace", nacosProperties.getNamespace());
			properties.put("serverAddr", nacosProperties.getServerAddr());
			ConfigService configService = NacosFactory.createConfigService(properties);
			configService.addListener(dataId, group, new Listener() {
				
				@Override
				public void receiveConfigInfo(String configInfo) {
					log.info("addListener recieve:" + configInfo);
				}
				
				@Override
				public Executor getExecutor() {

					return null;
				}
			});
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("addListener to Nacos Error , [" + e.getMessage() + "]");
			}
		}
	}

	@Override
	public void removeListener(NacosProperties nacosProperties) throws Exception {
		try {
			String dataId = nacosProperties.getDataId();
			String group = nacosProperties.getGroup();
			Properties properties = new Properties();
			properties.put("namespace", nacosProperties.getNamespace());
			properties.put("serverAddr", nacosProperties.getServerAddr());
			ConfigService configService = NacosFactory.createConfigService(properties);
			configService.removeListener(dataId, group, new Listener() {
				
				@Override
				public void receiveConfigInfo(String configInfo) {
					log.info("removeListener recieve:" + configInfo);
				}
				
				@Override
				public Executor getExecutor() {
					return null;
				}
			});
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("removeListener From Nacos Error , [" + e.getMessage() + "]");
			}
		}
	}

	@Override
	public boolean publishConfig(NacosProperties nacosProperties, String content) throws Exception {
		try {
			String dataId = nacosProperties.getDataId();
			String group = nacosProperties.getGroup();
			Properties properties = new Properties();
			properties.put("namespace", nacosProperties.getNamespace());
			properties.put("serverAddr", nacosProperties.getServerAddr());
			ConfigService configService = NacosFactory.createConfigService(properties);
			boolean isPublishOk  = configService.publishConfig(dataId, group, content);
			return isPublishOk ;
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("publishConfig to Nacos Error , [" + e.getMessage() + "]");
			}
			return false;
		}
	}

	@Override
	public boolean removeConfig(NacosProperties nacosProperties) {
		try {
			String dataId = nacosProperties.getDataId();
			String group = nacosProperties.getGroup();
			Properties properties = new Properties();
			properties.put("namespace", nacosProperties.getNamespace());
			properties.put("serverAddr", nacosProperties.getServerAddr());
			ConfigService configService = NacosFactory.createConfigService(properties);
			boolean isRemoveOk  = configService.removeConfig(dataId, group);
			return isRemoveOk ;
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("removeConfig to Nacos Error , [" + e.getMessage() + "]");
			}
			return false;
		}
	}
}
