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
package com.ymy.xxb.migrat.auth.shiro.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ymy.xxb.migrat.auth.shiro.constant.ShiroModuleConst;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.exception.CommonErrorCode;
import com.ymy.xxb.migrat.common.result.SoulResult;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author: wangyi
 *
 */
@ApiIgnore
@RestController
@RequestMapping(Constants.API_OPEN_PREFIX + ShiroModuleConst.MODULE_SSO)
public class ErrorController {
	
	@GetMapping("/loginUrl")
	public SoulResult loginUrl() {
		
		return SoulResult.warn(CommonErrorCode.UNAUTHENTICATED , "会话超时,请先登录！");
	}
	
	@GetMapping("/unauthorized")
	public SoulResult unauthorizedUrl() {
		
		return SoulResult.warn(CommonErrorCode.UNAUTHORISE , "抱歉，您的权限不足！");
	}
	
	@RequestMapping(value = "/unauth")
    public Object unauth() {
		return SoulResult.warn(CommonErrorCode.UNAUTHENTICATED , "会话超时,请先登录！");
    }
	
	@RequestMapping(value = "/kickout")
    public Object kickout() {
		return SoulResult.warn(CommonErrorCode.KICKOUT , "当前用户在其他地方登录,您已被强制登出,请确认是您本人操作！");
    }
	
}
