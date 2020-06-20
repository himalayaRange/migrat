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
package com.ymy.xxb.migrat.module.comyany.constant;

/**
 * 
 * The ModuleConst
 *
 * @author: wangyi
 *
 */
public interface ModuleConst {
	
	/**
	 * 需授权的API模块定义
	 *
	 * @author wangyi
	 */
	class API {
		/**
		 * The module of seata
		 */
		public static final String API_MODULE_SEATA = "/seata";		
		/**
		 * The module of monitor
		 */
		public static final String API_MODULE_MONITOR = "/monitor";
		
		/**
		 * The module of company
		 */
		public static final String API_MODULE_COMPANY = "/company";
		
		/**
		 * The module of department
		 */
		public static final String API_MODULE_DEPARTMENT = "/department";
		
		/**
		 * The module of dictionaryDetail
		 */
		public static final String API_MODULE_DICTIONARY_DETAIL = "/dictionaryDetail";
		
		/**
		 * The module of dictionaryMain
		 */
		public static final String API_MODULE_DICTIONARY_MAIN = "/dictionaryMain";
		
		/**
		 *  The module of bsUser
		 */
		public static final String API_MODULE_BS_USER = "/bsUser";
		
		/**
		 * The module of bsRole
		 */
		public static final String API_MODULE_BS_ROLE = "/bsRole";
		
		/**
		 * The module of bsMenu
		 */
		public static final String API_MODULE_BS_MENU = "/bsMenu";
		
		/**
		 * The module of comBox
		 */
		public static final String API_MODULE_COM_BOX = "/comBox";
		
		/**
		 * The module of cache
		 */
		public static final String API_MODULE_CACHAE = "/cache";
		
		/**
		 * The module of redission client server
		 */
		public static final String API_MODULE_REDISSION = "/rs";
		
	}
	
	/**
	 * 无需授权的API模块
	 *
	 * @author wangyi
	 */
	class Access {
		/**
		 * The module of login
		 */
		public static final String ACCESS_MODULE_LOGIN = "/sso";
	
	}
	
}
