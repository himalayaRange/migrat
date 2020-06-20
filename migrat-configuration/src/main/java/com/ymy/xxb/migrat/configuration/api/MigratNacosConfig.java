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
package com.ymy.xxb.migrat.configuration.api;

import com.ymy.xxb.migrat.configuration.properties.NacosProperties;

/**
 *
 * @author: wangyi
 *
 */
public interface MigratNacosConfig {
	
	/**
	 * 获取配置信息
	 * @return
	 * @throws Exception
	 */
	public String getConfig(NacosProperties properties) throws Exception;
	
	
	/**
	 * 添加监听
	 * @throws Exception
	 */
	public void addListener(NacosProperties properties) throws Exception;
	
	
	/**
	 * 删除监听， 取消监听配置，取消监听后配置不会再推送
	 * @throws Exception
	 */
	public void removeListener(NacosProperties properties) throws Exception;
	
	
	/**
	 * 发布配置,配置不存在则新增配置，配置存在则更新配置
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public boolean publishConfig(NacosProperties properties, String content) throws Exception;
	
	/**
	 * 删除配置
	 * @return
	 */
	public boolean removeConfig(NacosProperties properties);
	
}
