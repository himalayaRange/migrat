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
package com.ymy.xxb.migrat.module.biz.produce.service;

import com.ymy.xxb.migrat.auth.shiro.entity.BsRoleDO;
import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.common.service.BaseService;
import com.ymy.xxb.migrat.common.vo.BaseVo;
import com.ymy.xxb.migrat.module.biz.produce.entity.GroupRuleDO;
import com.ymy.xxb.migrat.module.biz.produce.entity.InformTemplateDO;
import com.ymy.xxb.migrat.module.biz.produce.mapper.InformTemplateRuleMapper;
import com.ymy.xxb.migrat.module.biz.produce.mapper.MessageSendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MessageSendService extends BaseService{
	
	@Autowired
	private MessageSendMapper messageSendMapper;

	@Autowired
	private InformTemplateRuleMapper informTemplateRuleMapper;

	@Override
	protected <T extends BaseVo> BaseMapper getBaseMapper() {

		return messageSendMapper;
	}

	public List<GroupRuleDO> getMessageSend(Map<String, Object> param) {
		return messageSendMapper.getMessageSend(param);
	}

	public List<GroupRuleDO> selectGroupRule(Map<String, Object> param) {
		return messageSendMapper.selectGroupRule(param);
	}

	public List<InformTemplateDO> selectInformTemplate(Map<String, Object> param) {
		return messageSendMapper.selectInformTemplate(param);
	}

	public BsRoleDO selectByRoleName(String roleName) {
		return messageSendMapper.selectByRoleName(roleName);

	}

	public GroupRuleDO selectByRuleName(String ruleName) {
		return messageSendMapper.selectByRuleName(ruleName);
	}

	public InformTemplateDO selectByTemplateTitle(String templateTitle) {
		return messageSendMapper.selectByTemplateTitle(templateTitle);
	}

	
}
