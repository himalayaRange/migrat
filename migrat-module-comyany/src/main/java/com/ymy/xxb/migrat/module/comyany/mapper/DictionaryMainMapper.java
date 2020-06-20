package com.ymy.xxb.migrat.module.comyany.mapper;

import java.util.List;
import java.util.Map;
import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.module.comyany.entity.DictionaryMainDO;

public interface DictionaryMainMapper extends BaseMapper{

	List<DictionaryMainDO> selectDictionaryTreeList(Map<String, Object> param);
}
