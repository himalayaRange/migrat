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

import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * BaseDO.
 *
 * @author wangyi
 */
@Data
public class BaseDO implements Serializable {

	private static final long serialVersionUID = 3336480126529035886L;

	/**
	 * ID由数据自增生产，针对数据非分库分表场景
     * primary key.
     * 
     */
    private Long id;
    
    /**
     * 数据有效性： 0 无效 1 有效
     */
    private Integer enabled;
    
    /**
     * created time.
     */
    private Timestamp dateCreated;
    
    /**
     * created user.
     */
    private String userCreated;

    /**
     * updated time.
     */
    private Timestamp dateUpdated;
    
    /**
     * updated user.
     */
    private String userUpdated;
}
