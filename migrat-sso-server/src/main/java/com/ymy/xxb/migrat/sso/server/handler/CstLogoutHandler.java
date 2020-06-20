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
package com.ymy.xxb.migrat.sso.server.handler;

import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * Logout Handler
 * @author: wangyi
 *
 */
@Component("cstLogoutHandler")
public class CstLogoutHandler implements LogoutHandler{
	
	
	   @Override
	   public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		try {
				String redirectUri = request.getParameter("redirect_uri"); //redirect_uri即为前端传来自定义跳转url地址
				request.getSession().invalidate();
				Cookie[] cookies = request.getCookies();
				for(Cookie cookie : cookies) {
					String name = cookie.getName();
					if("JSESSIONID".equals(name)) {
						cookie.setMaxAge(0);
					}
				}
				
				response.sendRedirect(redirectUri); //实现自定义重定向
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
