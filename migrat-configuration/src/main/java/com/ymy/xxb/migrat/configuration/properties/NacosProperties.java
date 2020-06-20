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
package com.ymy.xxb.migrat.configuration.properties;

import lombok.Data;

/**
 * nacos properties
 * @author: wangyi
 *
 */
@Data
public class NacosProperties {
	
	/**
	 * 命名空间，一般用于区分环境，如开发环境和正式环境
	 */
	private String namespace;
	
	/**
	 * nacos服务器地址
	 */
	private String serverAddr;
	
	/**
	 * 配置ID，采用类似 package.class（如com.taobao.tc.refund.log.level）的命名规则保证全局唯一性，class 部分建议是配置的业务含义。
	 * 全部字符小写。只允许英文字符和 4 种特殊字符（"."、":"、"-"、"_"），不超过 256 字节。
	 */
	private String dataId;
	
	
	/**
	 * 配置分组，建议填写产品名:模块名（Nacos:Test）保证唯一性，只允许英文字符和4种特殊字符（"."、":"、"-"、"_"），不超过128字节。
	 */
	private String group;
	
	
	/**
	 * 读取配置超时时间，单位ms,推荐3000
	 */
	private Long timeout;
	
}
