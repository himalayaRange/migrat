package com.ymy.xxb.migrat.module.comyany.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ymy.xxb.migrat.auth.shiro.vo.DeptTree;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Maps;
import com.ymy.xxb.migrat.auth.shiro.utils.ShiroContextUtil;
import com.ymy.xxb.migrat.common.annotation.ControllerEndpoint;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.page.PageParamUtil;
import com.ymy.xxb.migrat.common.page.PageUtils;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.comyany.constant.ModuleConst;
import com.ymy.xxb.migrat.auth.shiro.entity.DepartmentDO;
import com.ymy.xxb.migrat.auth.shiro.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 部门管理控制器
 * @author yl
 *
 */

@RestController
@RequestMapping(Constants.API_AUTH_PREFIX + ModuleConst.API.API_MODULE_DEPARTMENT)
@Api(value = "DepartmentController API", tags = "department", description = "部门管理控制器")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	/**
	 * 部门树，用于添加用户选择部门
	 * @param request
	 * @param response
	 * @param name
	 * @return
	 */
	@DS(Constants.AUTO_ROUTING)
	@GetMapping("/deptTree")
	@ApiOperation(value = "查询部门树" , httpMethod = "GET"  ,  notes = "根据参数获取部门树" , produces = "application/json;charset=UTF-8")
	public SoulResult initDepartmentTree(HttpServletRequest request, HttpServletResponse response,
								   @ApiParam(name="name", value="部门名称", required=false) @RequestParam(value="name",required=false) String name) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("name", name);
		DeptTree<DepartmentDO> departmentTree = departmentService.initDepartmentTree(param);

		return SoulResult.success("初始化部门树成功", departmentTree);
	}
	
	
	/**
	 * 分页查询部门信息
	 * @param request
	 * @param response
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping(value = "/select")
	@RequiresPermissions("dept:view")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "部门名称", name = "name", dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "部门编码", name = "code", dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "部门类别", name = "category", dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "当前页数", name = "page", dataType = "Integer", paramType = "query", required = true),
		@ApiImplicitParam(value = "每页显示条数", name = "rows", dataType = "Integer", paramType = "query", required = true)
	})
	@ApiOperation(value = "获取部门信息列表" , httpMethod = "GET"  ,  notes = "根据参数获取部门列表" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> param = PageParamUtil.pageParam(request);
			PageUtils<DepartmentDO> page = departmentService.selectPage(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}
	
	
	/**
	 * 新增部门信息
	 * @param request
	 * @param response
	 * @param departmentDO
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@PostMapping(value = "/insert")
	@RequiresPermissions("dept:add")
	@ControllerEndpoint(operation = "新增部门" , exceptionMessage = "新增部门失败")
	@ApiOperation(value = "新增部门" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增部门信息")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response, DepartmentDO departmentDO) {
		try {
			if (departmentDO == null 
					|| StringUtils.isEmpty(departmentDO.getName())
					|| StringUtils.isEmpty(departmentDO.getCode())
					|| StringUtils.isEmpty(departmentDO.getCategory())) {
				return SoulResult.warn("部门名称,部门编码,部门类别不能为空");
			}
			//校验部门名称，部门编码，部门类别
			Map<String, Object> param = Maps.newHashMap();
			// param.put("name", departmentDO.getName());
			// param.put("category", departmentDO.getCategory());
			param.put("code", departmentDO.getCode());
			List<DepartmentDO> list = departmentService.select(param);
			if(list != null && list.size() > 0){
				return SoulResult.warn("部门名称，部门编码，部门类别已存在");
			}
			//保存
			departmentDO.setCompanyId(ShiroContextUtil.currentUserCompanyId());
			departmentDO.setCreateUser(ShiroContextUtil.currentUsername());
			int temp = departmentService.insert(departmentDO);
			if (temp > 0) {
				return SoulResult.success("新增成功");
			} else {
				return SoulResult.error("新增失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("新增异常,ERROR:" + e.getMessage());
		}
	}
	
	
	/**
	 * 修改部门信息
	 * @param request
	 * @param response
	 * @param departmentDO
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@PostMapping(value = "/update")
	@RequiresPermissions("dept:update")
	@ControllerEndpoint(operation = "修改部门" , exceptionMessage = "修改部门失败")
	@ApiOperation(value = "修改部门信息" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "修改部门信息")
	public SoulResult update(HttpServletRequest request, HttpServletResponse response, DepartmentDO departmentDO) {
		try {
			if (departmentDO == null 
					|| StringUtils.isEmpty(departmentDO.getName())
					|| StringUtils.isEmpty(departmentDO.getCode())
					|| StringUtils.isEmpty(departmentDO.getCategory())) {
				return SoulResult.warn("部门名称,部门编码,部门类别不能为空");
			}
			departmentDO.setModifyUser(ShiroContextUtil.currentUsername());
			int temp = departmentService.update(departmentDO);
			if (temp > 0) {
				return SoulResult.success("修改成功");
			} else {
				return SoulResult.error("修改失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("修改异常,ERROR:" + e.getMessage());
		}
	}
	
	
	/**
	 * 按ID查询部门信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@GetMapping("/findById/{id}")
	@ApiOperation(value = "按ID查询", httpMethod="GET" , notes = "按部门ID查询部门信息" , produces = "application/json;charset=UTF-8")
	public SoulResult findById(
			@ApiParam(name="id",value="部门ID",required=true) @RequestParam(value="id",required=true) String id) {
		if(id == null){
			
			return SoulResult.warn("缺少参数：ID");
		}
		try {
			DepartmentDO department = departmentService.findById(id);
			
			return SoulResult.success("查询成功", department);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error(e.getMessage());
		}
	}
	
	/**
	 * 按部门ID逻辑删除部门信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.AUTO_ROUTING)
	@DeleteMapping("/delete")
	@RequiresPermissions("dept:delete")
	@ControllerEndpoint(operation = "删除部门" , exceptionMessage = "删除部门失败")
	@ApiOperation(value = "删除部门信息" ,httpMethod = "DELETE"  , notes = "按部门ID删除部门信息")
	public SoulResult delete(
			@ApiParam(name="id", value="部门ID",required=true)@RequestParam(value = "id", required = true) String id) {
		try {
			int temp = departmentService.logicDelete(id);
			if (temp > 0) {
				
				return SoulResult.success("删除成功");
			} else {
				
				return SoulResult.error("删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("删除异常,ERROR:" + e.getMessage());
		}
	}
}
