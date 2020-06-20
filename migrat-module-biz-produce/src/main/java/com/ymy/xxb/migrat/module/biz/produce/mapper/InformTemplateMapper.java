package com.ymy.xxb.migrat.module.biz.produce.mapper;

import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface InformTemplateMapper extends BaseMapper{

    List<Map<String, Object>> templeList(@Param("keyword") String keyword);

}
