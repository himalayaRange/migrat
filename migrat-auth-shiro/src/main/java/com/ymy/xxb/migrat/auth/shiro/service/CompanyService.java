package com.ymy.xxb.migrat.auth.shiro.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ymy.xxb.migrat.auth.shiro.entity.CompanyDO;
import com.ymy.xxb.migrat.auth.shiro.mapper.CompanyMapper;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.common.service.BaseService;
import com.ymy.xxb.migrat.common.vo.BaseVo;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CompanyService extends BaseService  {
	
	@Autowired
	private CompanyMapper companyMapper;
	
	@Override
	protected <T extends BaseVo> BaseMapper getBaseMapper() {
		
		return companyMapper;
	}
	
	/*-------------------自定义基类未实现接口-------------------*/
	
	@DS(value = Constants.DEFAULT_DS)
	public String maxTenantId() {
		return companyMapper.maxTenantId();
	}
	
	
	@DS(value = Constants.DEFAULT_DS)
	public List<CompanyDO> queryTenantInfo() {
		return companyMapper.queryTenantInfo();
	}
}
