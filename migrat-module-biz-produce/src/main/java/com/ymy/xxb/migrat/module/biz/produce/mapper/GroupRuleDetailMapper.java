package com.ymy.xxb.migrat.module.biz.produce.mapper;

import java.util.List;

import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.module.biz.produce.entity.GroupRuleDetailDO;

public interface GroupRuleDetailMapper extends BaseMapper{
	/**
	 *  根据规则rid 查询其对应的规则明细信息
	 * @param rid
	 * @return
	 */
	public List<GroupRuleDetailDO> findDetaiListByPid(String rid);
	
	/**
	 * 根据规则主键id删除原有明细
	 * @param rid
	 * @return
	 */
	public boolean deleteMxByRid(String rid);

}
