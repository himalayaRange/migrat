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
package com.ymy.xxb.migrat.auth.shiro.mapper;

import java.util.List;
import java.util.Map;
import com.ymy.xxb.migrat.auth.shiro.entity.BsMenuDO;
import com.ymy.xxb.migrat.common.mapper.BaseMapper;

/**
 * @author: wangyi
 */
public interface BsMenuMapper extends BaseMapper{
	
	/**
	 * 查询用户菜单
	 * @param param
	 * @return
	 */
	List<BsMenuDO> findUserMenus(Map<String, Object> param);
	
	/**
	 * 获取角色权限，菜单/按钮
	 * @param roleId
	 * @return
	 */
	List<BsMenuDO> getRolePerms(String roleId);
	
	/**
	 * 物理删除菜单
	 * @param id
	 * @return
	 */
	Integer physicalDelete(String id);
	
}
