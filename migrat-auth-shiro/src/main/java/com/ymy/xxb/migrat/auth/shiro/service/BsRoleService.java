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
package com.ymy.xxb.migrat.auth.shiro.service;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ymy.xxb.migrat.auth.shiro.entity.BsMenuDO;
import com.ymy.xxb.migrat.auth.shiro.entity.BsRoleMenuDO;
import com.ymy.xxb.migrat.auth.shiro.mapper.BsMenuMapper;
import com.ymy.xxb.migrat.auth.shiro.mapper.BsRoleMapper;
import com.ymy.xxb.migrat.auth.shiro.mapper.BsRoleMenuMapper;
import com.ymy.xxb.migrat.auth.shiro.mapper.BsUserRoleMapper;
import com.ymy.xxb.migrat.auth.shiro.realm.MyShiroRealm;
import com.ymy.xxb.migrat.auth.shiro.utils.TreeUtil;
import com.ymy.xxb.migrat.auth.shiro.vo.MenuTree;
import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.common.service.BaseService;
import com.ymy.xxb.migrat.common.utils.IdWorker;
import com.ymy.xxb.migrat.common.vo.BaseVo;

/**
 *
 * @author: wangyi
 *
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BsRoleService extends BaseService{
	
	@Autowired
	private BsMenuService bsMenuService;
	
	@Autowired
	private BsRoleMapper bsRoleMapper;
	
	@Autowired
	private BsUserRoleMapper bsUserRoleMapper;
	
	@Autowired
	private BsRoleMenuMapper bsRoleMenuMapper;
	
	@Autowired
	private BsMenuMapper bsMenuMapper;
	
	@Autowired
	private MyShiroRealm myShiroRealm;
	
	@Override
	protected <T extends BaseVo> BaseMapper getBaseMapper() {

		return bsRoleMapper;
	}
	
	/*-------------------自定义基类未实现接口-------------------*/
	public List<Map<String, Object>> droplist(String keyword) {
		return bsRoleMapper.droplist(keyword);
	}
	
	/**
	 * 分配菜单/按钮权限
	 * @param roleId 角色ID
	 * @param menuIds 菜单ID集合
	 */
	@Transactional
	public void assignPerms(String roleId, String menuIds) {
		// 查询角色菜单
		List<BsRoleMenuDO> list = bsRoleMenuMapper.selectRoleMenuByRoleId(roleId);
		if (list != null && list.size() > 0) {
			// 删除菜单
			bsRoleMenuMapper.removeRoleMenuByRoleId(roleId);
		}
		// 分配新的权限
		if (StringUtils.isNotEmpty(menuIds)) {
			String[] menus = menuIds.split(",");
			for (String menuId : menus) {
				BsRoleMenuDO bean = new BsRoleMenuDO();
				bean.setId(String.valueOf(new IdWorker().nextId()));
				bean.setBsRoleId(roleId);
				bean.setBsMenuId(menuId);
				bsRoleMenuMapper.insert(bean);
			}
		}
		// 清除shiro权限缓存
		myShiroRealm.clearCache();
	}
	
	/**
	 * 删除角色，同时删除角色下面的权限
	 * @param id
	 */
	@Transactional
	public void removeRole(String id) {
		// 删除角色
		bsRoleMapper.deleteRole(id);
		// 删除角色菜单
		bsRoleMenuMapper.removeRoleMenuByRoleId(id);
		// 删除用户角色
		bsUserRoleMapper.removeUserRoleByRoleId(id);
	}
	
	/**
	 * 通过角色ID获取权限信息
	 * @param roleId
	 * @return
	 */
	public MenuTree<BsMenuDO> getRolePermsTree(String roleId) {
		List<BsMenuDO> list = bsMenuMapper.getRolePerms(roleId);
		List<MenuTree<BsMenuDO>> menus = bsMenuService.convertMenus(list);
		return TreeUtil.buildMenuTree(menus);
	}
	
	public List<BsMenuDO> getRolePerms(String roleId) {
		List<BsMenuDO> list = bsMenuMapper.getRolePerms(roleId);
		return list;
	}
 	
}
