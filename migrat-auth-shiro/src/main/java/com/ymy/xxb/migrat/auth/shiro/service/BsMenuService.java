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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ymy.xxb.migrat.auth.shiro.constant.ShiroConst;
import com.ymy.xxb.migrat.auth.shiro.entity.BsMenuDO;
import com.ymy.xxb.migrat.auth.shiro.entity.BsUserDO;
import com.ymy.xxb.migrat.auth.shiro.mapper.BsMenuMapper;
import com.ymy.xxb.migrat.auth.shiro.mapper.BsUserMapper;
import com.ymy.xxb.migrat.auth.shiro.realm.MyShiroRealm;
import com.ymy.xxb.migrat.auth.shiro.utils.TreeUtil;
import com.ymy.xxb.migrat.auth.shiro.vo.MenuTree;
import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.common.service.BaseService;
import com.ymy.xxb.migrat.common.vo.BaseVo;

/**
 * @author: wangyi
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BsMenuService extends BaseService{
	
	@Autowired
	private BsUserMapper bsUserMapper;
	
	@Autowired
	private BsMenuMapper bsMenuMapper;
	
	@Autowired
	private MyShiroRealm myShiroRealm; 
	
	@Override
	protected <T extends BaseVo> BaseMapper getBaseMapper() {
		return bsMenuMapper;
	}
	
	/**
	 * 查找所有的菜单/按钮 （树形结构）
	 * @param param
	 * @return
	 */
	public MenuTree<BsMenuDO> initMenuTree(Map<String, Object> param) {
		List<BsMenuDO> menus = bsMenuMapper.select(param);
		List<MenuTree<BsMenuDO>> trees = this.convertMenus(menus);
		return TreeUtil.buildMenuTree(trees);
	}
	
	/**
	 * 查询当前用户的菜单/按钮 （树形结构）
	 * @param param
	 * @return
	 */
	public MenuTree<BsMenuDO> findUserMenus(Map<String, Object> param) {
		BsUserDO user = bsUserMapper.findById(String.valueOf(param.get("id")));
		if (ShiroConst.BS_USER_LEVEL_SAASADMIN.equals(user.getLevel())) {
			// 平台超级管理员获取所有菜单
			param.put("level", "1");
		} else {
			// 获取当前用户的菜单
			param.put("level", null);
		}
		List<BsMenuDO> menus = bsMenuMapper.findUserMenus(param);
		List<MenuTree<BsMenuDO>> trees = this.convertMenus(menus);
		return TreeUtil.buildMenuTree(trees);
	}
	
	/**
	 * 菜单转化为菜单树
	 * @param menus
	 * @return
	 */
	public List<MenuTree<BsMenuDO>> convertMenus(List<BsMenuDO> menus) {
	        List<MenuTree<BsMenuDO>> trees = new ArrayList<>();
	        menus.forEach(menu -> {
	            MenuTree<BsMenuDO> tree = new MenuTree<BsMenuDO>();
	            tree.setId(menu.getId());
	            tree.setParentId(menu.getPid());
	            tree.setTitle(menu.getMenuName());
	            tree.setIcon(menu.getIcon());
	            tree.setHref(menu.getUrl());
	            tree.setData(menu);
	            trees.add(tree);
	        });
	        return trees;
	    }
	
	@Transactional
	public Integer modifty(BsMenuDO bsMenuDO) {
		bsMenuDO.setModifyTime(new Date());
		Integer result = bsMenuMapper.update(bsMenuDO);
		myShiroRealm.clearCache();
		return result;
	}
	
	@Transactional
	public Integer physicalDelete(String id) {
		Integer result = bsMenuMapper.physicalDelete(id);
		myShiroRealm.clearCache();
		return result;
	}
	
}
