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
package com.ymy.xxb.migrat.coodinator.register.client;

import org.springframework.cloud.openfeign.FeignClient;
import com.ymy.xxb.migrat.coodinator.register.config.FeiginConfig;
import com.ymy.xxb.migrat.coodinator.register.constant.Const;

/**
 * 业务分支生产模块微服务
 *
 * @author: wangyi
 *
 */
@FeignClient(name = Const.SERVICE_ID_BIZ_PRODUCE, configuration = FeiginConfig.class)
public interface MicroServiceRouteBizProduceClient {

}
