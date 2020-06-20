package com.ymy.xxb.migrat.module.biz.produce.mapper;

import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.module.biz.produce.entity.InformLabelTemplateDO;
import org.apache.ibatis.annotations.Param;

public interface InformLabelTemplateMapper extends BaseMapper{

    Integer deleteById(String id);
    InformLabelTemplateDO findByLabelId(@Param("labelId") String labelId);
    InformLabelTemplateDO findByTempId(@Param("tempId") String tempId);

}
