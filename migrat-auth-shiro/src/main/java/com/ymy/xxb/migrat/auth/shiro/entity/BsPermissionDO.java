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

import com.ymy.xxb.migrat.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 权限实体
 * 
 * @author: wangyi
 *
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BsPermissionDO extends BaseVo{

	private static final long serialVersionUID = 2791573259855220133L;
	
	/**
	 * 权限父类ID,根目录定义为root
	 */
	@ApiModelProperty(value = "父类ID", name = "pid", dataType = "String", required = true)
	private String pid;

	/**
	 * 权限名称
	 */
	@ApiModelProperty(value = "权限名称", name="permissionName", dataType="String", required = true)
	private String permissionName;

	/**
	 * 权限类型： 1.菜单 2.按钮 3.API
	 */
	@ApiModelProperty(value = "权限类型： 1.菜单 2.按钮功能 3.API", name = "type", dataType="Integer", required = true)
	private Integer type;
	
	/**
	 * 权限编码
	 */
	@ApiModelProperty(value = "权限编码", name = "code" , dataType = "String", required = true)
	private String code;

	/**
	 * 权限描述
	 */
	@ApiModelProperty(value = "权限描述", name = "description" , dataType = "String")
	private String description;
	
	/**
	 * 可见状态
	 */
	@ApiModelProperty(value = "0：查询所有saas平台的最高权限，1：查询企业的权限", name = "enVisible", dataType = "String", required = true)
	private String enVisible;
}
