package com.ymy.xxb.migrat.module.comyany.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.ymy.xxb.migrat.module.comyany.entity.DictionaryDetailDO;
import com.ymy.xxb.migrat.module.comyany.service.DictionaryDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(Constants.API_AUTH_PREFIX + ModuleConst.API.API_MODULE_DICTIONARY_DETAIL)
@Api(value = "dictionaryDetailController API", tags = "dictionaryDetail", description = "字典明细控制器")
public class DictionaryDetailController {

	
	@Autowired
	private DictionaryDetailService dictionaryDetailService;
	
	
	/**
	 * 分页查询字典明细列表
	 * @param request
	 * @param response
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@GetMapping(value = "/select")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "编码", name = "code", dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "名称", name = "name", dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "当前页数", name = "page", dataType = "Integer", paramType = "query", required = true),
		@ApiImplicitParam(value = "每页显示条数", name = "rows", dataType = "Integer", paramType = "query", required = true)
	})
	@ApiOperation(value = "获取字典明细列表" , httpMethod = "GET" , notes = "根据参数获取字典明细列表" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> param = PageParamUtil.pageParam(request);
			PageUtils<DictionaryDetailDO> page = dictionaryDetailService.selectPage(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.error("查询异常 ,ERROR:" + e.getMessage());
		}
	}
	
	
	/**
	 * 新增字典明细信息
	 * @param request
	 * @param response
	 * @param dictionaryDetailDO
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@PostMapping(value = "/insert")
	@RequiresPermissions("dicDetail:add")
	@ApiOperation(value = "新增字典明细" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增字典明细信息")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response, DictionaryDetailDO dictionaryDetailDO) {
		try {
			if (dictionaryDetailDO == null 
					|| StringUtils.isEmpty(dictionaryDetailDO.getMid())
				  //|| StringUtils.isEmpty(String.valueOf(dictionaryDetailDO.getLevel()))
					|| StringUtils.isEmpty(dictionaryDetailDO.getCode())
					|| StringUtils.isEmpty(dictionaryDetailDO.getName())
					){
				return SoulResult.warn("父类,编码,名称不能为空");
			}
			//校验父类ID,级别,编码,名称
			Map<String, Object> param = Maps.newHashMap();
			param.put("mid", dictionaryDetailDO.getMid());
			param.put("level", dictionaryDetailDO.getLevel());
			param.put("code", dictionaryDetailDO.getCode());
			param.put("name", dictionaryDetailDO.getName());
			List<DictionaryDetailDO> list = dictionaryDetailService.select(param);
			if(list != null && list.size() > 0){
				return SoulResult.warn("父类,级别,编码,名称已存在");
			}
			//保存
			dictionaryDetailDO.setCreateUser(ShiroContextUtil.currentUsername());
			int temp = dictionaryDetailService.insert(dictionaryDetailDO);
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
	 * 修改字典明细信息
	 * @param request
	 * @param response
	 * @param dictionaryDetailDO
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@PostMapping(value = "/update")
	@RequiresPermissions("dicDetail:update")
	@ApiOperation(value = "修改字段明细信息" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "修改字段明细信息")
	public SoulResult update(HttpServletRequest request, HttpServletResponse response, DictionaryDetailDO dictionaryDetailDO) {
		try {
			if (dictionaryDetailDO == null 
					|| StringUtils.isEmpty(dictionaryDetailDO.getId())
					){
				return SoulResult.warn("ID不能为空");
			}
			dictionaryDetailDO.setModifyUser(ShiroContextUtil.currentUsername());
			int temp = dictionaryDetailService.update(dictionaryDetailDO);
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
	 * 按ID查询字典明细
	 * @param id
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@GetMapping("/findById/{id}")
	@ApiOperation(value = "字典明细ID", httpMethod="GET" , notes = "按ID查询字典明细信息" , produces = "application/json;charset=UTF-8")
	public SoulResult findById(
			@ApiParam(name="id",value="字典明细ID",required=true) @RequestParam(value="id",required=true) String id) {
		if(id == null){
			
			return SoulResult.warn("缺少参数：ID");
		}
		try {
			DictionaryDetailDO dictionaryDetailDO = dictionaryDetailService.findById(id);
			
			return SoulResult.success("查询成功", dictionaryDetailDO);
		} catch (Exception e) {
			
			return SoulResult.error(e.getMessage());
		}
	}
	
	
	/**
	 * 按ID逻辑删除字典明细信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@DeleteMapping("/delete")
	@RequiresPermissions("dicDetail:delete")
	@ApiOperation(value = "删除字典明细信息" ,httpMethod = "DELETE"  , notes = "按ID删除字典明细信息")
	public SoulResult delete(
			@ApiParam(name="id", value="字段明细ID",required=true)@RequestParam(value = "id", required = true) String id) {
		try {
			int temp = dictionaryDetailService.logicDelete(id);
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
