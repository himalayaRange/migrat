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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.sso.server.constant.LoginType;
import com.ymy.xxb.migrat.sso.server.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录失败后跳转
 * @author: wangyi
 *
 */
@Slf4j
@Component("cstAuthenticationFailureHandler")
public class CstAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	
	@Autowired
	private SecurityProperties securityProperties;
	
	//登录失败有很多原因，对应不同的异常
		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		                                    AuthenticationException e) throws IOException, ServletException {
			log.error("--- 登录失败 --");
			// 如果是JSON则把内容写回前台
			if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
				SoulResult error = SoulResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),"授权失败");
				response.getWriter().write(JSON.toJSONString(error));
			} else {
				super.onAuthenticationFailure(request, response, e);
			}
		}
}
