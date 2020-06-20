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

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;
import com.ymy.xxb.migrat.sso.server.entity.SysUserDO;

/**
 * 
 * @author: wangyi
 */
@Component
public class MyUserDetailsService implements UserDetailsService ,SocialUserDetailsService {
	
	@Autowired
	private SysUserService sysUserService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUserDO sysUserDO = sysUserService.selectByUsername(username);
		if (sysUserDO == null) {
			throw new UsernameNotFoundException("用户不存在！");
			// 返回带失败原因的数据对象
			// TODO
		} else {
			List<GrantedAuthority> grantedAuthorities = new ArrayList <>();
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ADMIN");
			//1：此处将权限信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
			grantedAuthorities.add(grantedAuthority);
			// 此处暂时不处理权限问题，只负责登录
			return new User(sysUserDO.getUsername() , sysUserDO.getPassword() , grantedAuthorities);
		}
	}

	/**
	 * 社交网站登录
	 */
	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		// TODO
		return null;
	}

}
