package com.ymy.xxb.migrat.module.biz.produce.mapper;

import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.module.biz.produce.entity.InformTemplateRuleDO;
import feign.Param;

public interface InformTemplateRuleMapper extends BaseMapper{

    Integer delete(String id);

    InformTemplateRuleDO findByTempleId(@Param("templeId") String templeId);
}
