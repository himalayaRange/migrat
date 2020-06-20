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
package com.ymy.xxb.migrat.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ymy.xxb.migrat.sso.server.entity.SysUserDO;
import com.ymy.xxb.migrat.sso.server.mapper.SysUserMapper;

/**
 *
 * @author: wangyi
 *
 */
@Service
public class SysUserService {
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	SysUserDO selectByUsername(String username) {
		
		return sysUserMapper.selectByUsername(username);
	}
	
	/**
	 * 通过ID查询用户
	 * @param id
	 * @return
	 */
	SysUserDO selectById(Long id) {
		
		return sysUserMapper.selectById(id);
	}
	
}
