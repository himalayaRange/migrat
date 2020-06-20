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
import com.ymy.xxb.migrat.auth.shiro.service.CompanyService;
import com.ymy.xxb.migrat.auth.shiro.utils.ShiroContextUtil;
import com.ymy.xxb.migrat.common.config.DS;
import com.ymy.xxb.migrat.common.constant.Constants;
import com.ymy.xxb.migrat.common.page.PageParamUtil;
import com.ymy.xxb.migrat.common.page.PageUtils;
import com.ymy.xxb.migrat.common.result.SoulResult;
import com.ymy.xxb.migrat.module.comyany.constant.ModuleConst;
import com.ymy.xxb.migrat.auth.shiro.entity.CompanyDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 企业公司控制器
 * 
 * @author wangyi
 *
 */
@RestController
@RequestMapping(Constants.API_AUTH_PREFIX + ModuleConst.API.API_MODULE_COMPANY)
@Api(value = "companyController API", tags = "company", description = "企业公司控制器")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	@DS(value = Constants.DEFAULT_DS)
	@GetMapping(value = "/select")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "公司ID", name = "id", dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "公司名称", name = "name", dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "企业登录账号ID", name = "managerId",dataType = "String", paramType = "query"),
		@ApiImplicitParam(value = "当前页数", name = "page", dataType = "Integer", paramType = "query", required = true),
		@ApiImplicitParam(value = "每页显示条数", name = "rows", dataType = "Integer", paramType = "query", required = true)
	})
	@RequiresPermissions("company:view")
	@ApiOperation(value = "获取公司信息列表" , httpMethod = "GET"  ,  notes = "根据参数获取公司列表" , produces = "application/json;charset=UTF-8")
	public SoulResult select(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> param = PageParamUtil.pageParam(request);
			PageUtils<CompanyDO> page = companyService.selectPage(param);
			return SoulResult.success("查询成功", page);
		} catch (Exception e) {
			e.printStackTrace();
			return SoulResult.success("查询异常 ,ERROR:" + e.getMessage());
		}
	}

	
	@DS(value = Constants.DEFAULT_DS)
	@PostMapping(value = "/insert")
	@RequiresPermissions("company:add")
	@ApiOperation(value = "新增公司" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "新增公司信息,非必输项为冗余字段")
	public SoulResult insert(HttpServletRequest request, HttpServletResponse response, CompanyDO companyDO) {
		try {
			if (companyDO == null 
					|| StringUtils.isEmpty(companyDO.getName())
					|| StringUtils.isEmpty(companyDO.getManagerId())) {
				return SoulResult.warn("公司名称和企业登录账号ID不能为空");
			}
			//校验公司名称和企业账户ID
			Map<String, Object> param = Maps.newHashMap();
			param.put("name", companyDO.getName());
			param.put("managerId", companyDO.getManagerId());
			List<CompanyDO> list = companyService.select(param);
			if(list != null && list.size() > 0){
				return SoulResult.warn("公司名称和企业登录账号ID已经存在");
			}
			//保存
			companyDO.setCreateUser(ShiroContextUtil.currentUsername());
			int temp = companyService.insert(companyDO);
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
	
	
	@DS(value = Constants.DEFAULT_DS)
	@PostMapping(value = "/update")
	@RequiresPermissions("company:update")
	@ApiOperation(value = "修改公司" ,httpMethod = "POST" , produces = "application/json;charset=UTF-8" , notes = "修改公司信息,非必输项为冗余字段")
	public SoulResult update(HttpServletRequest request, HttpServletResponse response, CompanyDO companyDO) {
		try {
			if (companyDO == null
					|| StringUtils.isEmpty(companyDO.getId())
					|| StringUtils.isEmpty(companyDO.getName())
					|| StringUtils.isEmpty(companyDO.getManagerId())) {
				return SoulResult.warn("ID、公司名称、企业登录账号ID不能为空");
			}
			companyDO.setModifyUser(ShiroContextUtil.currentUsername());
			int temp = companyService.update(companyDO);
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
	 * 按ID查询公司信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@GetMapping("/findById/{id}")
	@ApiOperation(value = "按ID查询", httpMethod="GET" , notes = "按公司ID查询公司信息" , produces = "application/json;charset=UTF-8")
	public SoulResult findById(
			@ApiParam(name="id",value="公司ID",required=true) @RequestParam(value="id",required=true) String id) {
		if(id == null){
			
			return SoulResult.warn("缺少参数：ID");
		}
		try {
			CompanyDO company = companyService.findById(id);
			
			return SoulResult.success("查询成功", company);
		} catch (Exception e) {
			
			return SoulResult.error(e.getMessage());
		}
	}
	
	/**
	 * 按公司ID逻辑删除公司信息
	 * @param id
	 * @return
	 */
	@DS(value = Constants.DEFAULT_DS)
	@DeleteMapping("/delete")
	@RequiresPermissions("company:delete")
	@ApiOperation(value = "删除公司信息" ,httpMethod = "DELETE"  , notes = "按公司ID删除公司信息")
	public SoulResult delete(
			@ApiParam(name="id", value="公司ID",required=true)@RequestParam(value = "id", required = true) String id) {
		try {
			int temp = companyService.logicDelete(id);
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
