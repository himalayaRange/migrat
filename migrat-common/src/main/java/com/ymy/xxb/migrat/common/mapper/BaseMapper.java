package com.ymy.xxb.migrat.common.mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.ymy.xxb.migrat.common.vo.BaseVo;

/**
 * @author wangyi
 */
public interface BaseMapper {
	/**
	 * 按条件查询条数
	 * @param param
	 * @return
	 */
	public Serializable count(Map<String, Object> param);

	/**
	 *  按条件查询数据
	 * @param param
	 * @return
	 */
	public <T extends BaseVo> List<T> select(Map<String, Object> param);
	
	/**
	 * 大数据查询
	 * @param param
	 * @return
	 */
	public <T extends BaseVo> List<T> largeDataSelect(Map<String, Object> param);

	/**
	 * 更新操作
	 * @param t
	 * @return
	 */
	public <T extends BaseVo> Integer update(T t);

	/**
	 * 插入操作
	 * @param t
	 * @return
	 */
	public <T extends BaseVo> Integer insert(T t);
	
	/**
	 * 逻辑删除：系统中暂不提供物理删除操作，如需要用户自行定义
	 * @param id
	 * @return
	 */
	public Integer logicDelete(@Param("id") String id);
	
	/**
	 * 按ID查询数据
	 * @param id
	 * @return
	 */
	public <T extends BaseVo> T findById(@Param("id") String id);
}
