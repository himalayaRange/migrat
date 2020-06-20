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
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.sso.server.constant.LoginType;
import com.ymy.xxb.migrat.sso.server.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录成功后的条状
 * 
 * @author: wangyi
 *
 */
@Slf4j
@Component
public class CstAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
			log.info("-----SSO LOGIN SUCCESS----");
			// 如果是JSON则把内容写回前台
			if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
				SoulResult success = SoulResult.success("授权成功", HttpStatus.OK.getReasonPhrase());
				response.getWriter().write(JSON.toJSONString(success));
			} else {
				super.onAuthenticationSuccess(request, response, authentication);
			}
	}

}
