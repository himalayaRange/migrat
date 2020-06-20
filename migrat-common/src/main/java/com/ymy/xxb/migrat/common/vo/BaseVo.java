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

package com.ymy.xxb.migrat.common.vo;

import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * JsonFormat: 后端传数据到前端格式转换
 * DateTimeFormat: 前端数据到后端的格式转换
 * 
 * @author wangyi
 */
@Data
public class BaseVo implements Serializable{
	
	private static final long serialVersionUID = 1342644169004050386L;
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 租户ID
	 */
	@ApiModelProperty(value = "ID", name = "tenantId", dataType = "String", hidden = false)
	private String tenantId;
	
	/**
	 * 分布式
	 */
	@ApiModelProperty(value = "主键ID", name="id", dataType="String", required=true)
	private String id;
	
	/**
     * 创建时间
     */
	@DateTimeFormat(pattern = DATE_FORMAT)
	@JsonFormat(pattern = DATE_FORMAT, timezone = "GMT+8")
	@ApiModelProperty(value = "创建时间", name = "createTime", dataType="Date", required = true)
    private Date createTime;
    
    /**
     * 创建人
     */
	@JsonIgnore
	@ApiModelProperty(value = "创建人", name = "createUser")
    private String createUser;
    
    /**
     * 修改时间
     */
	@DateTimeFormat(pattern = DATE_FORMAT)
	@JsonFormat(pattern = DATE_FORMAT, timezone = "GMT+8")
	@ApiModelProperty(value = "修改时间", name = "modifyTime", dataType="Date")
    private Date modifyTime;
    
    /**
     * 修改人
     */
	@JsonIgnore
	@ApiModelProperty(value = "修改人", name = "modifyUser")
    private String modifyUser;
	
	/**
	 * 创建人名称
	 */
	@ApiModelProperty(hidden = true)
	private String createUserName;
	
	/**
	 * 修改人名称
	 */
	@ApiModelProperty(hidden = true)
	private String modiftyUserName;
}
