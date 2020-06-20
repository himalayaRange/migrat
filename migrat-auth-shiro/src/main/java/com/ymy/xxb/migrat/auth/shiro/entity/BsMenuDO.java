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
 * 菜单DO
 * @author: wangyi
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BsMenuDO extends BaseVo{

	private static final long serialVersionUID = 7092443648305609625L;

	/**
	 * 菜单父类ID,根目录定义为ROOT
	 */
	@ApiModelProperty(value = "父类ID", name = "pid", dataType = "String", required = true)
	private String pid;

	/**
	 * 菜单/按钮名称
	 */
	@ApiModelProperty(value = "菜单按钮名称", name="menuName", dataType="String", required = true)
	private String menuName; 

	/**
	 * 菜单/按钮URL
	 */
	@ApiModelProperty(value = "菜单/按钮URL", name="url", dataType="String")
	private String url;

	/**
	 * 权限标识
	 */
	@ApiModelProperty(value = "perms", name="perms", dataType="String")
	private String perms;
	
	/**
	 * 图标
	 */
	@ApiModelProperty(value = "图标", name = "icon" , dataType = "String")
	private String icon;
	
	/**
	 * 菜单类型： 1.菜单 2.按钮
	 */
	@ApiModelProperty(value = "权限类型： 0.菜单 1.按钮", name = "type", dataType="Integer", required = true)
	private Integer type;
	
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序", name = "orderNum" , dataType = "String")
	private String orderNum;
	
	/**
	 * 权限描述
	 */
	@ApiModelProperty(value = "权限描述", name = "description" , dataType = "String")
	private String description;
	
	/**
	 * 可见状态
	 */
	@ApiModelProperty(value = "0：启用，1：禁用", name = "enVisible", dataType = "String", required = true)
	private String enVisible;
}
