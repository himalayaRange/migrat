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

package com.ymy.xxb.migrat.common.constant;

/**
 * Constants.
 *
 * @author wangyi
 */
public interface Constants {

	/**
	 * 项目数据库命名空间前缀
	 */
	public static final String PROJECT_DATABASE_NAMESPACE_PREFIX = "migrat";

	/**
	 * 项目默认数据源标记
	 */
	public static final String DEFAULT_DS = "migrat";
	
	/**
	 * 自动路由到当前租户指定的数据库
	 */
	public static final String AUTO_ROUTING = "auto_routing";
	
	/**
	 * 手动路由到指点的数据库
	 */
	public static final String MANUAL_ROUTING = "manual_routing";
	
	/**
	 * 数据库命名空间前缀，后面通过_num来区分主数据和业务数据库
	 */
	public static final String TENANT_DATABASE_NAMESPACE_PREFIX = Constants.PROJECT_DATABASE_NAMESPACE_PREFIX;

	/**
	 * 租户ID分组单元基数，10个租户一个数据库
	 */
	public static final Integer TENANT_ID_GROUP_UNIT = 10;

	/**
	 * shiro redis 缓存的键设置
	 */
	public static final String CACHE_PERMISSION_KEY = "CACHE_PERMISSION_KEY";
		
	/**
	 * 用户路由信息缓存
	 */
	public static final String CACHE_USERS_TENANT_ROUTING = "CACHE_USERS_TENANT_ROUTING";
	
	/**
	 * 系统启动标志
	 */
	public static final String STARTED_FLAG = "STARTED_FLAG";
	
	/**
	 * 异步线程池名称
	 */
	public static final String ASYNC_POOL = "migratAsyncThreadPool";
	
	/**
	 * The constant SIGN.
	 */
	public static final String SIGN = "sign";

	/**
	 * The constant COLONS.
	 */
	public static final String COLONS = ":";

	/**
	 * 需要授权的API前缀
	 */
	public static final String API_AUTH_PREFIX = "/authenticationApi";

	/**
	 * 开放API前缀
	 */
	public static final String API_OPEN_PREFIX = "/openApi";
	
}
