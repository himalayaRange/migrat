package com.ymy.xxb.migrat.common.service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.common.page.PageUtils;
import com.ymy.xxb.migrat.common.utils.IdWorker;
import com.ymy.xxb.migrat.common.vo.BaseVo;

/**
 * Commons Service
 * @author wangyi
 *
 */
public abstract class BaseService {
	
	protected abstract <T extends BaseVo> BaseMapper getBaseMapper();

	/**
	 * 分页查询
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends BaseVo> PageUtils<T> selectPage(Map<String, Object> param) {
		if (param == null) {
			param = new HashMap<String, Object>();
		}
		List<BaseVo> list = select(param);
		PageUtils<T> pageUtil = new PageUtils(list, count(param));
		if(param.get("page") != null){
			pageUtil.setNowpage(Integer.parseInt(param.get("page").toString()));
		}
		if(param.get("rows") != null){
			pageUtil.setPagesize(Integer.parseInt(param.get("rows").toString()));
		}		
		if(param.get("rowStart") != null){
			pageUtil.setRowStart(Integer.parseInt(param.get("rowStart").toString()));
		}
		if(param.get("rowEnd") != null){
			pageUtil.setRowEnd(Integer.parseInt(param.get("rowEnd").toString()));
		}
		
		return pageUtil;
	}
	
	/**
	 * 分页查询 返回object
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	public <Object> PageUtils<Object> selectPageInfo(Map<String, Object> param) {
		if (param == null) {
			param = new HashMap<String, Object>();
		}
		List<Object> list = (List<Object>) select((Map<String, java.lang.Object>) param);
		PageUtils<Object> pageUtil = new PageUtils(list, count((Map<String, java.lang.Object>) param));
		if(param.get("page") != null){
			pageUtil.setNowpage(Integer.parseInt(param.get("page").toString()));
		}
		if(param.get("rows") != null){
			pageUtil.setPagesize(Integer.parseInt(param.get("rows").toString()));
		}		
		if(param.get("rowStart") != null){
			pageUtil.setRowStart(Integer.parseInt(param.get("rowStart").toString()));
		}
		if(param.get("rowEnd") != null){
			pageUtil.setRowEnd(Integer.parseInt(param.get("rowEnd").toString()));
		}
		
		return pageUtil;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	public <Object> PageUtils<Object> selectLargeDataPageInfo(Map<String, Object> param) {
		if (param == null) {
			param = new HashMap<String, Object>();
		}
		List<Object> list = (List<Object>) largeDataSelect((Map<String, java.lang.Object>) param);
		PageUtils<Object> pageUtil = new PageUtils(list, count((Map<String, java.lang.Object>) param));
		if(param.get("page") != null){
			pageUtil.setNowpage(Integer.parseInt(param.get("page").toString()));
		}
		if(param.get("rows") != null){
			pageUtil.setPagesize(Integer.parseInt(param.get("rows").toString()));
		}		
		if(param.get("rowStart") != null){
			pageUtil.setRowStart(Integer.parseInt(param.get("rowStart").toString()));
		}
		if(param.get("rowEnd") != null){
			pageUtil.setRowEnd(Integer.parseInt(param.get("rowEnd").toString()));
		}
		
		return pageUtil;
	}

	/**
	 * 查询功能，返回list
	 */
	public <T extends BaseVo> List<T> select(Map<String, Object> param) {
		return getBaseMapper().select(param);
	}
	
	/**
	 * 大数据量分页查询
	 * @param param
	 * @return
	 */
	public <T extends BaseVo> List<T> largeDataSelect(Map<String, Object> param){
		return getBaseMapper().largeDataSelect(param);
	}

	/**
	 * 返回总记录数
	 */
	public long count(Map<String, Object> param) {
		Serializable count = getBaseMapper().count(param);
		if (count == null) {
			return 0L;
		} else {
			return Long.valueOf(count.toString());
		}
	}

	/**
	 * 逻辑删除
	 */
	public int logicDelete(String id) {
		return getBaseMapper().logicDelete(id);
	}

	/**
	 * 插入记录
	 */
	public <T extends BaseVo> int insert(T t) {
		// snowflake生成分布式唯一ID
		t.setId(String.valueOf(new IdWorker().nextId()));
		t.setCreateTime(new Date());
		return getBaseMapper().insert(t);
	}

	/**
	 * 更新记录
	 */
	public <T extends BaseVo> int update(T t) {
		t.setModifyTime(new Date());
		return getBaseMapper().update(t);
	}

	/**
	 * 获取特定对象
	 */
	public <T extends BaseVo> T findById(String id) {
		return getBaseMapper().findById(id);
	}
}
