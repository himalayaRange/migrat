package com.ymy.xxb.migrat.module.comyany.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.ymy.xxb.migrat.module.comyany.entity.DictionaryMainDO;
import com.ymy.xxb.migrat.module.comyany.utils.TreeUtil;
import com.ymy.xxb.migrat.module.comyany.vo.DictionaryMainTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.common.service.BaseService;
import com.ymy.xxb.migrat.common.vo.BaseVo;
import com.ymy.xxb.migrat.module.comyany.mapper.DictionaryDetailMapper;
import com.ymy.xxb.migrat.module.comyany.mapper.DictionaryMainMapper;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictionaryMainService extends BaseService{

	@Autowired
	private DictionaryMainMapper dictionaryMainMapper;
	
	@Autowired
	private DictionaryDetailMapper dictionaryDetailMapper;
	
	@Override
	protected <T extends BaseVo> BaseMapper getBaseMapper() {
		return dictionaryMainMapper;
	}

	
	/*-------------------自定义基类未实现接口-------------------*/
	
	public List<Map<String, Object>> queryDictionaryDetail(String id, String categoryCode) {
		return dictionaryDetailMapper.queryDictionaryDetail(id, categoryCode);
	}
	

	/**
	 * 查找所有的字典类别(树形结构)
	 * @return
	 */
	public DictionaryMainTree<DictionaryMainDO> initDictionaryMainTree(Map<String, Object> param) {
		List<DictionaryMainDO> dictionaryMains = dictionaryMainMapper.selectDictionaryTreeList(param);
		List<DictionaryMainTree<DictionaryMainDO>> trees = this.convertDictionaryMains(dictionaryMains);
		DictionaryMainTree<DictionaryMainDO> dictionaryMainDODictionaryMainTree = TreeUtil.buildDictionaryMainTree(trees);
		return dictionaryMainDODictionaryMainTree;
	}

	/**
	 * 字典类别转化为字典类别树
	 * @param dictionaryMains
	 * @return
	 */
	public List<DictionaryMainTree<DictionaryMainDO>> convertDictionaryMains(List<DictionaryMainDO> dictionaryMains) {
		List<DictionaryMainTree<DictionaryMainDO>> trees = new ArrayList<>();
		dictionaryMains.forEach(dictionaryMain -> {
			DictionaryMainTree<DictionaryMainDO> tree = new DictionaryMainTree<DictionaryMainDO>();
			tree.setId(dictionaryMain.getId());
			tree.setParentId(dictionaryMain.getParentId());
			tree.setTitle(dictionaryMain.getCategoryName());
			tree.setData(dictionaryMain);
			tree.setType("1"); // 1代表字典分类树数据
			trees.add(tree);
		});
		return trees;
	}

}
