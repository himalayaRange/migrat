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

import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.common.service.BaseService;
import com.ymy.xxb.migrat.common.vo.BaseVo;
import com.ymy.xxb.migrat.module.biz.produce.entity.BasicModuleDO;
import com.ymy.xxb.migrat.module.biz.produce.mapper.BasicModuleMapper;
import com.ymy.xxb.migrat.module.biz.produce.utils.TreeUtil;
import com.ymy.xxb.migrat.module.biz.produce.vo.BasicModuleTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BasicModuleService extends BaseService{
	
	@Autowired
	private BasicModuleMapper basicModuleMapper;
	
	@Override
	protected <T extends BaseVo> BaseMapper getBaseMapper() {

		return basicModuleMapper;
	}

	/*-------------------自定义基类未实现接口-------------------*/

	/**
	 * 查找所有的基础模块配置(树形结构)
	 * @param param
	 * @return
	 */
	public BasicModuleTree<BasicModuleDO> initBasicModuleTree(Map<String, Object> param) {
		List<BasicModuleDO> basicModules = basicModuleMapper.select(param);
		List<BasicModuleTree<BasicModuleDO>> trees = this.convertBasicModules(basicModules);
		BasicModuleTree<BasicModuleDO> buildBasicModuleTree = TreeUtil.buildBasicModuleTree(trees);
		return buildBasicModuleTree;
	}

	/**
	 * 基础模块配置转化为基础模块配置树
	 * @param basicModules
	 * @return
	 */
	public List<BasicModuleTree<BasicModuleDO>> convertBasicModules(List<BasicModuleDO> basicModules) {
		List<BasicModuleTree<BasicModuleDO>> trees = new ArrayList<>();
		basicModules.forEach(basicModule -> {
			BasicModuleTree<BasicModuleDO> tree = new BasicModuleTree<BasicModuleDO>();
			tree.setId(basicModule.getId());
			tree.setParentId(basicModule.getPid());
			tree.setTitle(basicModule.getModuleName());
			tree.setData(basicModule);
			trees.add(tree);
		});
		return trees;
	}
	
}
