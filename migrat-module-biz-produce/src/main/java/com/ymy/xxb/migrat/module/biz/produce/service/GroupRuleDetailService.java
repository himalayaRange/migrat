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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.common.service.BaseService;
import com.ymy.xxb.migrat.common.vo.BaseVo;
import com.ymy.xxb.migrat.module.biz.produce.entity.GroupRuleDetailDO;
import com.ymy.xxb.migrat.module.biz.produce.mapper.GroupRuleDetailMapper;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class GroupRuleDetailService extends BaseService{
	
	@Autowired
	private GroupRuleDetailMapper groupRuleDetailMapper;
	
	/**
	 *  根据规则rid 查询其对应的规则明细信息
	 * @param  rid
	 * @return
	 */
	public List<GroupRuleDetailDO> findDetaiListByPid(String rid){
		return groupRuleDetailMapper.findDetaiListByPid(rid);
	}
	

	/**
	 *  根据规则rid 删除其对应的规则明细信息
	 * @param  rid
	 * @return
	 */
	public boolean deleteMxByRid(String rid){
		return groupRuleDetailMapper.deleteMxByRid(rid);
	}
	
	@Override
	protected <T extends BaseVo> BaseMapper getBaseMapper() {

		return groupRuleDetailMapper;
	}
	
}
