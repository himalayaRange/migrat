package com.ymy.xxb.migrat.module.comyany.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.ymy.xxb.migrat.common.mapper.BaseMapper;

public interface DictionaryDetailMapper extends BaseMapper{
	
	List<Map<String, Object>> queryDictionaryDetail(@Param("id") String id, @Param("categoryCode")String categoryCode);
	
}
