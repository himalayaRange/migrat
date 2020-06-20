package com.ymy.xxb.migrat.auth.shiro.mapper;

import java.util.List;
import com.ymy.xxb.migrat.auth.shiro.entity.CompanyDO;
import com.ymy.xxb.migrat.common.mapper.BaseMapper;

/**
 * @author wangyi
 *
 */
public interface CompanyMapper extends  BaseMapper{
		
	String maxTenantId();
	
	List<CompanyDO> queryTenantInfo();
}
