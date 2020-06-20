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
package com.ymy.xxb.migrat.module.comyany.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ymy.xxb.migrat.configuration.api.support.MigratNacosConfigSupport;
import com.ymy.xxb.migrat.configuration.properties.NacosProperties;

/**
 * 
 * Redisson Config
 *
 * @author: wangyi
 *
 */
//@Component
public class MyRedissonConfig{
	private static final Long TIMEOUT = 3000L;

	@Autowired
	private MigratNacosConfigSupport migratNacosConfigSupport;
	
	@Value("${spring.cloud.nacos.config.server-addr}")
	private String serverAddr;
	
	@Value("${spring.cloud.nacos.config.namespace}")
	private String namespace;
	
	@Value("${spring.cloud.nacos.config.ext-config[4].data-id}")
	private String dataId;
	
	
	public String fromYaml()  {
		NacosProperties nacosProperties = new NacosProperties();
		nacosProperties.setNamespace(namespace);
		nacosProperties.setServerAddr(serverAddr);
		nacosProperties.setDataId(dataId);
		nacosProperties.setTimeout(TIMEOUT);
		String yaml;
		try {
			yaml = migratNacosConfigSupport.getConfig(nacosProperties);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Pull Config From Nacos Failed, To See Exception Detail: " + e.getMessage());
		}
		return yaml;
	} 
	
}
