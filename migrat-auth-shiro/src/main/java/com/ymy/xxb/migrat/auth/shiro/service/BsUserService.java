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
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.ymy.xxb.migrat.auth.shiro.entity.BsRoleDO;
import com.ymy.xxb.migrat.auth.shiro.entity.BsUserDO;
import com.ymy.xxb.migrat.auth.shiro.entity.BsUserRoleDO;
import com.ymy.xxb.migrat.auth.shiro.mapper.BsUserMapper;
import com.ymy.xxb.migrat.auth.shiro.mapper.BsUserRoleMapper;
import com.ymy.xxb.migrat.auth.shiro.realm.MyShiroRealm;
import com.ymy.xxb.migrat.auth.shiro.utils.ShiroContextUtil;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
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
public class BsUserService extends BaseService{
	@Autowired
	private MyShiroRealm myShiroRealm;
	
	@Autowired
	private BsUserMapper bsUserMapper;
	
	@Autowired
	private BsUserRoleMapper bsUserRoleMapper;
	
	@Override
	protected <T extends BaseVo> BaseMapper getBaseMapper() {

		return bsUserMapper;
	}
	
	/*-------------------自定义基类未实现接口-------------------*/
	
	/**
	 * 通过ID或手机号查询用户基本信息
	 * @param param
	 * @return
	 */
	public BsUserDO getUserSimpleInfo(Map<String, Object> param) {
		return bsUserMapper.getUserSimpleInfo(param);
	}
	
	/**
	 * 根据用户名或手机号查询用户信息
	 * 
	 * @param usernameOrMobile 用户名 或 手机号
	 * @return BsUserDO
	 */
	@DS(value = Constants.AUTO_ROUTING)
	public BsUserDO findByUsernameOrMobile(String usernameOrMobile) throws Exception{
		BsUserDO bsUserDO = bsUserMapper.findByUsernameOrMobile(usernameOrMobile);
		// 查询用户角色
		if (bsUserDO != null) {
			Set<BsRoleDO> bsRoles = bsUserMapper.findBsRoles(bsUserDO.getId());
			for (BsRoleDO bsRole : bsRoles) {
				String id = bsRole.getId();
				Set<String> perms = bsUserMapper.findBsPermissions(id);
				bsRole.setBsPermissions(JSON.toJSONString(perms));
			}
			bsUserDO.setBsRoles(JSON.toJSONString(bsRoles));
		}
		return bsUserDO;
	}
	
	/**
	 * 添加用户
	 * @param bsUserDO
	 */
	@Transactional
	public void addUser(BsUserDO bsUserDO) {
		// 添加用户主表
		this.insert(bsUserDO);
		// 添加用户角色表
		String roleDropList = bsUserDO.getRoleDropList();
		if (roleDropList != null && (!roleDropList.equals(""))) {
			String[] roleIds = roleDropList.split(",");
			for(String roleId : roleIds) {
				BsUserRoleDO bean = new BsUserRoleDO();
				bean.setId(String.valueOf(new IdWorker().nextId())); // 雪花算法
				bean.setBsRoleId(roleId);
				bean.setBsUserId(bsUserDO.getId());
				bsUserRoleMapper.insert(bean);
			}
		}
	}
	
	/**
	 * 修改用户
	 * @param bsUserDO
	 */
	@Transactional
	public void modiftyUser(BsUserDO bsUserDO) {
		// 更新主表
		this.update(bsUserDO);
		// 删除原始角色
		bsUserRoleMapper.removeUserRoleByUserId(bsUserDO.getId());
		// 添加新的角色
		String roleDropList = bsUserDO.getRoleDropList();
		if (roleDropList != null && (!roleDropList.equals(""))) {
			String[] roleIds = roleDropList.split(",");
			for(String roleId : roleIds) {
				BsUserRoleDO bean = new BsUserRoleDO();
				bean.setId(String.valueOf(new IdWorker().nextId())); // 雪花算法
				bean.setBsRoleId(roleId);
				bean.setBsUserId(bsUserDO.getId());
				bsUserRoleMapper.insert(bean);
			}
		}
		// 清除shiro权限缓存
		String currentUsername = ShiroContextUtil.currentUsername();
		if (StringUtils.equalsIgnoreCase(currentUsername, bsUserDO.getUsername())) {
			myShiroRealm.clearCache();
        }
	}
	
	/**
	 * 删除用户
	 * @param id
	 */
	@Transactional
	public void deleleUser(String id) {
		BsUserDO bsUserDO = bsUserMapper.findById(id);
		if (bsUserDO != null) {
			// 删除用户主表
			Integer result = bsUserMapper.deleteUser(id);
			if (result == 1) {
				// 删除用户角色信息
				bsUserRoleMapper.removeUserRoleByUserId(id);
			}
		}
	}
	
	/**
	 * 查询用户所属租户信息
	 * @return
	 */
	@DS(value = Constants.MANUAL_ROUTING)
	public List<Map<String, Object>> selectSimpleUsers() {
		// 查询租户的数量
		return bsUserMapper.selectSimpleUsers();
	}
}
