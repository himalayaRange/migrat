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
package com.ymy.xxb.migrat.auth.shiro.entity;

import java.util.Date;
import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *  SysOperLog
 * 
 * @author: wangyi
 *
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysOperLogDO extends BaseVo {

	private static final long serialVersionUID = -4722530175986376835L;

	/**
	 * 操作人，存储手机号
	 */
	private String operUser;

	/**
	 * 操作内容
	 */
	private String operation;

	/**
	 * 耗时
	 */
	private Long time;

	/**
	 * IP
	 */
	private String ip;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 位置
	 */
	private String location;
	
	/**
	 * 操作方法
	 */
	private String method;

	/**
	 * 方法参数
	 */
	private String params;

	/**
	 * 租户ID
	 */
	private String tenantId;
	
}
