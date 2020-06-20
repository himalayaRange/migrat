package com.ymy.xxb.migrat.module.biz.produce.mapper;

import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface GroupRuleMapper extends BaseMapper{

    List<Map<String, Object>> ruleList(@Param("keyword") String keyword);
	

}
