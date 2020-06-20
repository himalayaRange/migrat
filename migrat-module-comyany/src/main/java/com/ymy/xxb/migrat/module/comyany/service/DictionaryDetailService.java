package com.ymy.xxb.migrat.module.comyany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.common.service.BaseService;
import com.ymy.xxb.migrat.common.vo.BaseVo;
import com.ymy.xxb.migrat.module.comyany.mapper.DictionaryDetailMapper;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictionaryDetailService extends BaseService{

	@Autowired
	private DictionaryDetailMapper dictionaryDetailMapper;
	
	
	@Override
	protected <T extends BaseVo> BaseMapper getBaseMapper() {
		return dictionaryDetailMapper;
	}
	
	
	/*-------------------自定义基类未实现接口-------------------*/

}
