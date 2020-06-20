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
package com.ymy.xxb.migrat.sso.server.mapper;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import com.ymy.xxb.migrat.sso.server.entity.SysUserDO;

/**
 *
 * @author: wangyi
 *
 */
@Mapper
public interface SysUserMapper {
	/**
	 * 通过用户名查询用户
	 * @param username
	 * @return
	 */
	SysUserDO selectByUsername(@Param("username") String username);
	
	/**
	 * 通过ID查询用户
	 * @param id
	 * @return
	 */
	SysUserDO selectById(@Param("id") Long id);
	
}
