package com.ymy.xxb.migrat.module.comyany.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ymy.xxb.migrat.module.comyany.vo.DictionaryMainTree;
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
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.page.PageParamUtil;
import com.ymy.xxb.migrat.common.page.PageUtils;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.comyany.constant.ModuleConst;
import com.ymy.xxb.migrat.module.comyany.entity.DictionaryMainDO;
import com.ymy.xxb.migrat.module.comyany.service.DictionaryMainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(Constants.API_AUTH_PREFIX + ModuleConst.API.API_MODULE_DICTIONARY_MAIN)
@Api(value = "DictionaryMainController API", tags = "dictionaryMain", description = "字典类别控制器")
public class DictionaryMainController {

	@Autowired
	private DictionaryMainService dictionaryMainService;


	@DS(Constants.DEFAULT_DS)
	@GetMapping("/dictionaryMainTree")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "字典主表ID", name = "id", dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "类别编码", name = "categoryCode", dataType = "String", paramType = "query")
	})
	@ApiOperation(value = "查询字典类别树" , httpMethod = "GET"  ,  notes = "根据参数获取字典类别" , produces = "application/json;charset=UTF-8")
	public SoulResult dictionaryMainTree(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> param = PageParamUtil.pageParam(request);
		DictionaryMainTree<DictionaryMainDO> dictionaryMainTree = dictionaryMainService.initDictionaryMainTree(param);

		return SoulResult.success("初始化字典类别树成功", dictionaryMainTree);
	}
	
	/**
	 * 分页查询字典类别列表
	 * @param request
	 * @param response
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@GetMapping(value = "/select")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "类别编码", name = "categoryCode", dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "类别名称", name = "categoryName", dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "当前页数", name = "page", dataType = "Integer", paramType = "query", required = true),
		@ApiImplicitParam(value = "每页显示条数", name = "rows", dataType = "Integer", paramType = "query", required = true)
	})
	@ApiOperation(value = "获取字典类别列表" , httpMethod = "GET" , notes = "根据参数获取字典类别列表" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> param = PageParamUtil.pageParam(request);
			PageUtils<DictionaryMainDO> page = dictionaryMainService.selectPage(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}
	
	
	/**
	 * 新增字典类别信息
	 * @param request
	 * @param response
	 * @param dictionaryMainDO
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@PostMapping(value = "/insert")
	@RequiresPermissions("dicMain:add")
	@ApiOperation(value = "新增字典类别" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增字典类别信息")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response, DictionaryMainDO dictionaryMainDO) {
		try {
			if (dictionaryMainDO == null 
					|| StringUtils.isEmpty(dictionaryMainDO.getCategoryCode())
					|| StringUtils.isEmpty(dictionaryMainDO.getCategoryName())){
				return SoulResult.warn("类别编码,类别名称不能为空");
			}
			//校验类别编码,类别名称
			Map<String, Object> param = Maps.newHashMap();
			param.put("categoryCode", dictionaryMainDO.getCategoryCode());
			param.put("categoryName", dictionaryMainDO.getCategoryName());
			List<DictionaryMainDO> list = dictionaryMainService.select(param);
			if(list != null && list.size() > 0){
				return SoulResult.warn("类别编码,类别名称已存在");
			}
			//保存
			dictionaryMainDO.setCreateUser(ShiroContextUtil.currentUsername());
			int temp = dictionaryMainService.insert(dictionaryMainDO);
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
	 * 修改字典类别信息
	 * @param request
	 * @param response
	 * @param dictionaryMainDO
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@PostMapping(value = "/update")
	@RequiresPermissions("dicMain:update")
	@ApiOperation(value = "修改字段类别信息" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "修改字段类别信息")
	public SoulResult update(HttpServletRequest request, HttpServletResponse response, DictionaryMainDO dictionaryMainDO) {
		try {
			if (dictionaryMainDO == null 
					|| StringUtils.isEmpty(dictionaryMainDO.getCategoryCode())
					|| StringUtils.isEmpty(dictionaryMainDO.getCategoryName())){
				return SoulResult.warn("类别编码,类别名称不能为空");
			}
			dictionaryMainDO.setModifyUser(ShiroContextUtil.currentUsername());
			int temp = dictionaryMainService.update(dictionaryMainDO);
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
	 * 按ID查询字典类别信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@GetMapping("/findById/{id}")
	@ApiOperation(value = "字典类别ID", httpMethod="GET" , notes = "按ID查询字典类别信息" , produces = "application/json;charset=UTF-8")
	public SoulResult findById(
			@ApiParam(name="id",value="字典类别ID",required=true) @RequestParam(value="id",required=true) String id) {
		if(id == null){
			
			return SoulResult.warn("缺少参数：ID");
		}
		try {
			DictionaryMainDO dictionaryMainDO = dictionaryMainService.findById(id);
			
			return SoulResult.success("查询成功", dictionaryMainDO);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error(e.getMessage());
		}
	}
	
	
	/**
	 * 按ID逻辑删除字典类别信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@DeleteMapping("/delete")
	@RequiresPermissions("dicMain:delete")
	@ApiOperation(value = "删除字典类别信息" ,httpMethod = "DELETE"  , notes = "按ID删除字典类别信息")
	public SoulResult delete(
			@ApiParam(name="id", value="字段类别ID",required=true)@RequestParam(value = "id", required = true) String id) {
		try {
			int temp = dictionaryMainService.logicDelete(id);
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
	
	
	@DS(value = Constants.DEFAULT_DS)
	@PostMapping("/queryDictionaryDetail")
	@ApiOperation(value = "获取字典明细" , httpMethod = "POST" , notes = "根据字典表ID,获取字典明细对应信息")
	public SoulResult queryDictionaryDetail(
			@ApiParam(name="id", value="字典表主表ID",required = false)@RequestParam(value = "id", required = false) String id,
			@ApiParam(name="categoryCode", value="字典分类",required = false)@RequestParam(value = "categoryCode", required = false) String categoryCode
			) {
		
		if(id == null && categoryCode == null){
			
			return SoulResult.warn("id和分类不能都为空");
		}
		
		try{
			List<Map<String, Object>> list = dictionaryMainService.queryDictionaryDetail(id, categoryCode);
			
			return SoulResult.success(list);
		} catch(Exception e) {
			
			return SoulResult.error("查询异常,ERROR:" + e.getMessage());
		}
	}
}
