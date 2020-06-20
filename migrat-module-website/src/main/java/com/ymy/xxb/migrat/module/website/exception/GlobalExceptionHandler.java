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

package com.ymy.xxb.migrat.module.website.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.ymy.xxb.migrat.common.result.SoulResult;
import lombok.extern.slf4j.Slf4j;

/**
 * ControllerMethodResolver.
 * https://dzone.com/articles/global-exception-handling-with-controlleradvice
 *
 * @author wangyi
 */
@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
	
    @ExceptionHandler(Exception.class)
    public SoulResult handleException(Exception ex) {
    	log.error(ex.getMessage(), ex);
        String msg = "系统错误，请稍后再试，INFO: {" + ex.getMessage() + "}";
        return SoulResult.error(msg);
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public SoulResult handleAuthorizationException(UnauthorizedException ex) {
    	String msg = "数据绑定错误，INFO:" + "{" + ex.getMessage() + "}";
    	return SoulResult.warn(msg);
    }
    
    @ExceptionHandler(BindException.class)
    public SoulResult handleBindException(BindException ex) {
    	String msg = "数据绑定错误，INFO:" + "{" + ex.getMessage() + "}";
    	return SoulResult.warn(msg);
    }
    
}
