package com.ymy.xxb.migrat.module.biz.produce.mapper;

import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.module.biz.produce.entity.InformNodeDO;
import java.util.List;
import java.util.Map;

public interface InformNodeMapper extends BaseMapper{

    List<InformNodeDO> selectAll(Map<String, Object> param);

    InformNodeDO selectByNodeName(String nodeName, Integer sort);
}
