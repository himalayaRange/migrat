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
package com.ymy.xxb.migrat.zuul.filter;

import java.util.Map;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Maps;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 *
 * @author: wangyi
 *
 */
@RestController
public class ErrorHandlerFilter implements ErrorController{

	@Override
	public String getErrorPath() {
		return "/error";
	}
	
	 @RequestMapping("/error")
     public Object error(){
         // RequestContext ctx = RequestContext.getCurrentContext();
         // ZuulException exception = (ZuulException)ctx.getThrowable();
         // int nStatusCode = exception.nStatusCode;
         // String message = exception.getMessage();
         Map<String, Object> rest = Maps.newHashMap();
         rest.put("success", false);
         rest.put("code", 429);
         rest.put("message", "前方太拥挤，请稍后再试...");
         rest.put("data", null);
         return rest;
     }

}
