package com.ymy.xxb.migrat.auth.shiro.service;

import com.ymy.xxb.migrat.auth.shiro.entity.DepartmentDO;
import com.ymy.xxb.migrat.auth.shiro.utils.TreeUtil;
import com.ymy.xxb.migrat.auth.shiro.vo.DeptTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ymy.xxb.migrat.auth.shiro.mapper.DepartmentMapper;
import com.ymy.xxb.migrat.common.mapper.BaseMapper;
import com.ymy.xxb.migrat.common.service.BaseService;
import com.ymy.xxb.migrat.common.vo.BaseVo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DepartmentService extends BaseService{

	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Override
	protected <T extends BaseVo> BaseMapper getBaseMapper() {
		
		return departmentMapper;
	}
	
	/*-------------------自定义基类未实现接口-------------------*/
	
	/**
	 * 查找所有的部门(树形结构)
	 * @param param
	 * @return
	 */
    public DeptTree<DepartmentDO> initDepartmentTree(Map<String, Object> param) {
		List<DepartmentDO> depatrments = departmentMapper.select(param);
		List<DeptTree<DepartmentDO>> trees = this.convertDepartments(depatrments);
		DeptTree<DepartmentDO> departmentDODeptTree = TreeUtil.buildDeptTree(trees);
		return departmentDODeptTree;
	}

	/**
	 * 部门转化为部门树
	 * @param departments
	 * @return
	 */
	public List<DeptTree<DepartmentDO>> convertDepartments(List<DepartmentDO> departments) {
		List<DeptTree<DepartmentDO>> trees = new ArrayList<>();
		departments.forEach(department -> {
			DeptTree<DepartmentDO> tree = new DeptTree<DepartmentDO>();
			tree.setId(department.getId());
			tree.setParentId(department.getParentId());
			tree.setTitle(department.getName());
			tree.setData(department);
			trees.add(tree);
		});
		return trees;
	}

}
