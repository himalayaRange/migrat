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
package com.ymy.xxb.migrat.gateway.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Maps;
import reactor.core.publisher.Mono;

/**
 * Gateway fallback
 * @author: wangyi
 *
 */
@RestController
public class FallbackController {
	
	@RequestMapping("/fallback")
	public Mono<Map<String, Object>> fallback() {
		Map<String, Object> fallback = Maps.newHashMap();
		fallback.put("success", false);
		fallback.put("code", 408);
		fallback.put("message", "哎呀，系统太忙了，请稍后再试吧！");
		fallback.put("data", null);
		return Mono.just(fallback);
	}
	
}
